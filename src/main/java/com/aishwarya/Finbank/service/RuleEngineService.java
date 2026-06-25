package com.aishwarya.Finbank.service;

import com.aishwarya.Finbank.model.LoanApplication;
import com.aishwarya.Finbank.ruleengine.evaluator.StaticRulesEvaluator;
import com.aishwarya.Finbank.model.Rule;
import com.aishwarya.Finbank.ruleengine.evaluator.RulesEvaluator;
import com.aishwarya.Finbank.ruleengine.loader.RuleLoader;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class RuleEngineService {

    private final RulesEvaluator rulesEvaluator;

    private final RuleLoader ruleLoader;

    public RuleEngineService(RuleLoader ruleLoader, RulesEvaluator rulesEvaluator) {
        this.ruleLoader = ruleLoader;
        this.rulesEvaluator = rulesEvaluator;
    }

    public void evaluateLoanApplication(LoanApplication application) {
        List<Rule> rules = ruleLoader.loadRules(application.getLoanType());
        rulesEvaluator.evaluateRules(application, rules);
    }
}
