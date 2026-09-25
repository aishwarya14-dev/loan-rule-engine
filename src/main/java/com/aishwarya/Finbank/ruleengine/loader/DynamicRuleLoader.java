package com.aishwarya.Finbank.ruleengine.loader;
import com.aishwarya.Finbank.exceptions.DslParsingException;
import com.aishwarya.Finbank.metrics.RuleEngineMetrics;
import com.aishwarya.Finbank.model.DslRule;
import com.aishwarya.Finbank.model.LoanType;
import com.aishwarya.Finbank.model.LoanTypeFactorConfig;
import com.aishwarya.Finbank.model.Rule;
import com.aishwarya.Finbank.repository.RuleRepository;
import com.aishwarya.Finbank.ruleengine.parser.DslRulesParser;
import com.aishwarya.Finbank.service.LoanTypeFactorConfigService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


@Slf4j
@Component
@Primary
@AllArgsConstructor
public class DynamicRuleLoader implements RuleLoader {

    private final RuleRepository repository;
    private final DslRulesParser parser;
    private final LoanTypeFactorConfigService loanTypeFactorConfigService;
    private final RuleEngineMetrics metrics;

    // locking mechanism to prevent cache evict while another request thread is reading rules for a loan type
    private final ConcurrentHashMap<String, ReadWriteLock> locks
            = new ConcurrentHashMap<>();

    // in-memory loaded rules populated after first load to prevent thundering herd on cache miss
    private final ConcurrentHashMap<String, List<Rule>> localCache
            = new ConcurrentHashMap<>();

    private ReadWriteLock getLockForLoanType(String loanType) {
        return locks.computeIfAbsent(loanType,
                k -> new ReentrantReadWriteLock());
    }

    // Cache per loan type : key = HOME_LOAN, CAR_LOAN etc
    @Cacheable(value = "rules", key = "#loanType.loanType")
    @Override
    public List<Rule> loadRules(LoanType loanType) {

        // checking the local cache for this loan type to avoid thundering herd
        List<Rule> cached = localCache.get(loanType.getLoanType());
        if (cached != null) {
            log.debug("Local cache hit for: {}", loanType.getLoanType());
            return cached;
        }

        // acquire the lock to fetch rules from cache for this loan type
        ReadWriteLock lock = getLockForLoanType(loanType.getLoanType());

        log.info("Loading rules from DB for {}", loanType.getLoanType());
        List<Rule> rules = new ArrayList<>();

        // lock before fetching
        lock.readLock().lock();
        try {
            synchronized (lock) {
                // recheck in local cache if the rules have been fetched by some other thread to prevent thundering herd
                cached = localCache.get(loanType.getLoanType());
                if (cached != null) return cached;

                rules = loadRulesFromDatabase(loanType);
                localCache.put(loanType.getLoanType(), rules);
            }
        } catch (DataAccessException e) {
            log.error("Failed to fetch rules for loan type: {}", loanType.getLoanType(), e);
        }
        finally {
            lock.readLock().unlock();
        }
        return rules;
    }

    private List<Rule> loadRulesFromDatabase(LoanType loanType){
        List<DslRule> entities = repository.findByLoanTypeLoanType(loanType.getLoanType());
        List<Rule> rules = new ArrayList<>();
        for (DslRule dslRule : entities) {
            try {
                Rule parsedRule = parser.parseDslRule(dslRule.getDslRule());
                metrics.incrementDslParseSuccess();
                parsedRule.setEvidenceWeight(dslRule.getEvidenceWeight());
                parsedRule.setSeverity(dslRule.getRuleSeverity());
                LoanTypeFactorConfig loanTypeFactorConfig = loanTypeFactorConfigService.getLoanTypeFactorConfig(dslRule.getLoanType().getId(),dslRule.getFactor().getId());
                parsedRule.setImportanceLevel(loanTypeFactorConfig.getImportanceLevel().getWeight());
                parsedRule.setFactorId(loanTypeFactorConfig.getFactor().getId());
                parsedRule.setLoanTypeId(loanTypeFactorConfig.getLoanType().getId());

                rules.add(parsedRule);
            } catch (DslParsingException e) {
                metrics.incrementDslParseFailed();
                log.error("Failed to parse DSL rule: {}", dslRule.getDslRule(), e);
            } catch (Exception e) {
                metrics.incrementDslParseFailed();
                log.error("Unexpected error while parsing DSL rule: {}", dslRule.getDslRule(), e);
            }
        }
        return rules;
    }

    // Evict only the affected loan type when a new rule is created
    @CacheEvict(value = "rules_v2", key = "#loanType.loanType")
    public void evictByLoanType(LoanType loanType) {
        ReadWriteLock lock = getLockForLoanType(loanType.getLoanType());
        lock.writeLock().lock();

        try {
            log.info("Cache evicted for loan type: {}",
                    loanType.getLoanType());
            metrics.incrementCacheEviction();
        } finally {
            lock.writeLock().unlock();
        }
    }
}
