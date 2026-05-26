package com.aishwarya.FinBank.service;

import com.aishwarya.FinBank.model.LoanApplication;
import com.aishwarya.FinBank.ruleengine.evaluator.RulesEvaluator;
import com.aishwarya.FinBank.ruleengine.evaluator.StaticRulesEvaluator;
import com.aishwarya.FinBank.ruleengine.loader.RuleLoader;
import com.aishwarya.FinBank.model.Rule;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RuleEngineService {

    private final RulesEvaluator rulesEvaluator;

    private final RuleLoader ruleLoader;

    public RuleEngineService(RuleLoader ruleLoader , RulesEvaluator rulesEvaluator){
        this.ruleLoader = ruleLoader;
        this.rulesEvaluator = rulesEvaluator;
    }

    public void evaluateLoanApplication(LoanApplication application) {
            List<Rule> rules = ruleLoader.loadRules(application.getLoanType());
             rulesEvaluator.evaluateRules(application, rules);
    }
}
