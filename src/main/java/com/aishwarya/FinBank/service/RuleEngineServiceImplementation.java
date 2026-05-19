package com.aishwarya.FinBank.service;

import com.aishwarya.FinBank.model.LoanApplication;
import com.aishwarya.FinBank.ruleengine.evaluator.DynamicRulesEvaluator;
import com.aishwarya.FinBank.ruleengine.evaluator.StaticRulesEvaluator;
import com.aishwarya.FinBank.ruleengine.loader.RuleLoader;
import com.aishwarya.FinBank.model.Rule;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class RuleEngineServiceImplementation implements RuleEngineService{

    private StaticRulesEvaluator staticEvaluator;

    private DynamicRulesEvaluator dslEvaluator;

    private RuleLoader staticRuleLoader;

    private RuleLoader dynamicRuleLoader;


    public void evaluateLoanApplication(LoanApplication application,String mode) {
        if ("STATIC".equalsIgnoreCase(mode)) {
            List<Rule> rules = staticRuleLoader.loadRules(application.getLoanType());
            boolean result = staticEvaluator.evaluateRules(application, rules);
        }
        else if ("DSL".equalsIgnoreCase(mode)) {
            List<Rule> rules = dynamicRuleLoader.loadRules(application.getLoanType());
            boolean result = dslEvaluator.evaluateRules(application, rules);
        }
        else {
            throw new IllegalArgumentException("Invalid mode: " + mode);
        }
    }
}
