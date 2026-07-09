package com.aishwarya.Finbank.service;

import com.aishwarya.Finbank.model.LoanApplication;
import com.aishwarya.Finbank.model.LoanApplicationResult;
import com.aishwarya.Finbank.ruleengine.evaluator.StaticRulesEvaluator;
import com.aishwarya.Finbank.model.Rule;
import com.aishwarya.Finbank.ruleengine.evaluator.RulesEvaluator;
import com.aishwarya.Finbank.ruleengine.loader.RuleLoader;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class RuleEngineService {

    private final RulesEvaluator rulesEvaluator;

    private final RuleLoader ruleLoader;

    public LoanApplicationResult evaluateLoanApplication(LoanApplication application) {
        log.info("Starting evaluation for loan application with ID: {}", application.getId());
        List<Rule> rules = ruleLoader.loadRules(application.getLoanType());
        log.info("Evaluating loan application with ID: {} using {} rules", application.getId(), rules.size());
        return (LoanApplicationResult) rulesEvaluator.evaluateRules(application, rules);
    }
}
