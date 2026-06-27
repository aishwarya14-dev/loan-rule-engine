package com.aishwarya.Finbank.ruleengine.loader;
import com.aishwarya.Finbank.exceptions.DslParsingException;
import com.aishwarya.Finbank.model.DslRule;
import com.aishwarya.Finbank.model.LoanType;
import com.aishwarya.Finbank.model.LoanTypeFactorConfig;
import com.aishwarya.Finbank.model.Rule;
import com.aishwarya.Finbank.repository.LoanTypeFactorConfigRepo;
import com.aishwarya.Finbank.repository.RuleRepository;
import com.aishwarya.Finbank.ruleengine.parser.DslRulesParser;
import com.aishwarya.Finbank.service.LoanTypeFactorConfigService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
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

    private RuleRepository repository;

    private DslRulesParser parser;

    private LoanTypeFactorConfigService loanTypeFactorConfigService;

    // Cache per loan type name : key = "HOME_LOAN", "CAR_LOAN" etc
    @Cacheable(value = "rules", key = "#loanType.loanType")
    @Override
    public List<Rule> loadRules(LoanType loanType) {
        log.info("Loading rules from DB for {}", loanType.getLoanType());
        List<Rule> rules = new ArrayList<>();
        try {
            List<DslRule> entities = repository.findByLoanTypeLoanType(loanType.getLoanType());
            for (DslRule dslRule : entities) {
                try {
                    Rule parsedRule = parser.parseDslRule(dslRule.getDslRule());
                    parsedRule.setEvidenceWeight(dslRule.getEvidenceWeight());
                    parsedRule.setSeverity(dslRule.getRuleSeverity());
                    LoanTypeFactorConfig loanTypeFactorConfig = loanTypeFactorConfigService.getLoanTypeFactorConfig(dslRule.getLoanType().getId(),dslRule.getFactor().getId());
                    parsedRule.setImportanceLevel(loanTypeFactorConfig.getImportanceLevel().getWeight());
                    parsedRule.setFactorId(loanTypeFactorConfig.getFactor().getId());
                    parsedRule.setLoanTypeId(loanTypeFactorConfig.getLoanType().getId());

                    rules.add(parsedRule);
//                    System.out.println(Hibernate.isInitialized(loanTypeFactorConfig.getFactor()));
                } catch (DslParsingException e) {
                    log.error("Failed to parse DSL rule: {}", dslRule.getDslRule(), e);
                } catch (Exception e) {
                    log.error("Unexpected error while parsing DSL rule: {}", dslRule.getDslRule(), e);
                }
            }
        } catch (DataAccessException e) {
            log.error("Failed to fetch rules for loan type: {}", loanType.getLoanType(), e);
        }
        return rules;
    }

    // Evict only the affected loan type when a new rule is created
    @CacheEvict(value = "rules", allEntries = true)
    public void evictByLoanType(LoanType loanType) {
    }
}
