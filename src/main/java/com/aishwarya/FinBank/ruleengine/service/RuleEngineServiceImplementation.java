package com.aishwarya.FinBank.ruleengine.service;

import com.aishwarya.FinBank.model.LoanApplication;
import com.aishwarya.FinBank.ruleengine.evaluator.DynamicRulesEvaluator;
import com.aishwarya.FinBank.ruleengine.evaluator.StaticRulesEvaluator;
import com.aishwarya.FinBank.ruleengine.loader.RuleLoader;
import com.aishwarya.FinBank.ruleengine.model.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RuleEngineServiceImplementation implements RuleEngineService{

    @Autowired
    private StaticRulesEvaluator staticEvaluator;

    @Autowired
    DynamicRulesEvaluator dslEvaluator;

    @Autowired
    @Qualifier("staticRuleLoader")
    private RuleLoader staticRuleLoader;


    public void evaluateLoanApplication(LoanApplication application,String mode) {
        if ("STATIC".equalsIgnoreCase(mode)) {
            List<Rule> rules = staticRuleLoader.loadRules();
            boolean result = staticEvaluator.evaluateRules(application,rules);
        }
//        else if ("DSL".equalsIgnoreCase(mode)) {
//            staticEvaluator.evaluate(application);
//        }
        else {
            throw new IllegalArgumentException("Invalid mode: " + mode);
        }
    }
}
