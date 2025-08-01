package com.aishwarya.FinBank.ruleengine.service;

import com.aishwarya.FinBank.model.LoanApplication;
import com.aishwarya.FinBank.ruleengine.evaluator.DslRulesEvaluator;
import com.aishwarya.FinBank.ruleengine.evaluator.RuleEvaluator;
import com.aishwarya.FinBank.ruleengine.evaluator.StaticRulesEvaluator;
import com.aishwarya.FinBank.ruleengine.loader.StaticRuleLoader;
import com.aishwarya.FinBank.ruleengine.model.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RuleEngineServiceImplementation implements RuleEngineService{

    @Autowired
    StaticRulesEvaluator staticEvaluator;

    @Autowired
    DslRulesEvaluator dslEvaluator;

    @Autowired
    private StaticRuleLoader ruleLoader;


    public void evaluateLoanApplication(LoanApplication application,String mode) {
        if ("STATIC".equalsIgnoreCase(mode)) {
            List<Rule> rules = ruleLoader.loadRules();
            staticEvaluator.evaluate(application,rules);
        } else if ("DSL".equalsIgnoreCase(mode)) {
//            staticEvaluator.evaluate(application);
        }
        else {
            throw new IllegalArgumentException("Invalid mode: " + mode);
        }
    }
}
