package com.aishwarya.Finbank.service;

import com.aishwarya.Finbank.model.LoanApplication;
import com.aishwarya.Finbank.ruleengine.evaluator.StaticRulesEvaluator;
import com.aishwarya.Finbank.model.Rule;
import com.aishwarya.Finbank.ruleengine.evaluator.RulesEvaluator;
import com.aishwarya.Finbank.ruleengine.loader.RuleLoader;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class RuleEngineService {

    private static final Logger log = LoggerFactory.getLogger(RuleEngineService.class);
    private final RulesEvaluator rulesEvaluator;

    private final RuleLoader ruleLoader;

    public void evaluateLoanApplication(LoanApplication application) {
        log.info("Starting evaluation for loan application with ID: {}", application.getId());
        List<Rule> rules = ruleLoader.loadRules(application.getLoanType());
        log.info("Evaluating loan application with ID: {} using {} rules", application.getId(), rules.size());
        rulesEvaluator.evaluateRules(application, rules);
    }
}
