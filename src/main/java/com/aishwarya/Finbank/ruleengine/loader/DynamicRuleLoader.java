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


@Slf4j
@Component
@Primary
@AllArgsConstructor
public class DynamicRuleLoader implements RuleLoader {

    private final RuleRepository repository;

    private final DslRulesParser parser;

    private final LoanTypeFactorConfigService loanTypeFactorConfigService;

    private final RuleEngineMetrics metrics;

    // Cache per loan type : key = "HOME_LOAN", "CAR_LOAN" etc
    @Cacheable(value = "rules", key = "#loanType.loanType")
    @Override
    public List<Rule> loadRules(LoanType loanType) {
        log.info("Loading rules from DB for {}", loanType.getLoanType());
        List<Rule> rules = new ArrayList<>();
        try {
            List<DslRule> entities = repository.findByLoanTypeLoanType(loanType.getLoanType());
            for (DslRule dslRule : entities) {
                try {
                    Rule parsedRule = metrics.recordDslParseDuration(
                            () -> parser.parseDslRule(dslRule.getDslRule())
                    );
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
        } catch (DataAccessException e) {
            log.error("Failed to fetch rules for loan type: {}", loanType.getLoanType(), e);
        }
        return rules;
    }

    // Evict only the affected loan type when a new rule is created
    @CacheEvict(value = "rules", key = "#loanType.loanType")
    public void evictByLoanType(LoanType loanType) {
        metrics.incrementCacheEviction();
        log.info("Cache evicted for: {}", loanType.getLoanType());
    }
}
