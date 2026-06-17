package com.aishwarya.FinBank.ruleengine.loader;
import com.aishwarya.FinBank.exceptions.DslParsingException;
import com.aishwarya.FinBank.exceptions.LoanEvaluationException;
import com.aishwarya.FinBank.model.DslRule;
import com.aishwarya.FinBank.model.LoanType;
import com.aishwarya.FinBank.model.Rule;
import com.aishwarya.FinBank.ruleengine.parser.DslRulesParser;
import com.aishwarya.FinBank.repository.RuleRepository;
import lombok.extern.slf4j.Slf4j;
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
public class DynamicRuleLoader implements RuleLoader {

    @Autowired
    private RuleRepository repository;
    @Autowired
    private DslRulesParser parser;

    // Cache per loan type name : key = "HOME_LOAN", "CAR_LOAN" etc
    @Cacheable(value = "rules", key = "#loanType.loanType")
    @Override
    public List<Rule> loadRules(LoanType loanType) {
        List<Rule> rules = new ArrayList<>();
        try {
            List<DslRule> entities = repository.findByLoanTypeLoanType(loanType.getLoanType());
            for(DslRule dslRule : entities) {
                try {
                    Rule parsedRule = parser.parseDslRule(dslRule.getDslRule());
                    rules.add(parsedRule);
                }
                catch (DslParsingException e) {
                    log.error("Failed to parse DSL rule: {}", dslRule.getDslRule(), e);
                } catch (Exception e){
                    log.error("Unexpected error while parsing DSL rule: {}", dslRule.getDslRule(), e);
                }
            }
        }
        catch (DataAccessException e) {
            log.error("Failed to fetch rules for loan type: {}", loanType.getLoanType(), e);
        }
        return rules;
    }

    // Evict only the affected loan type when a new rule is created
    @CacheEvict(value = "rules", key = "#loanType.loanType")
    public void evictByLoanType(LoanType loanType) {}
}
