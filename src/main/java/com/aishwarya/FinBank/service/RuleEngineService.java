package com.aishwarya.FinBank.service;

import com.aishwarya.FinBank.model.LoanApplication;
import com.aishwarya.FinBank.ruleengine.evaluator.RulesEvaluator;
import com.aishwarya.FinBank.ruleengine.loader.RuleLoader;
import com.aishwarya.FinBank.model.Rule;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class RuleEngineService {

    private RulesEvaluator rulesEvaluator;

    private RuleLoader ruleLoader;


    public void evaluateLoanApplication(LoanApplication application) {
            List<Rule> rules = ruleLoader.loadRules(application.getLoanType());
            boolean result = rulesEvaluator.evaluateRules(application, rules);
    }
}
