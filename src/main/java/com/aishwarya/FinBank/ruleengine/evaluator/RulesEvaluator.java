package com.aishwarya.FinBank.ruleengine.evaluator;

import com.aishwarya.FinBank.model.LoanApplication;
import com.aishwarya.FinBank.ruleengine.model.RulePOJO;

import java.util.List;

public interface RulesEvaluator {
    public boolean evaluateRules(LoanApplication application, List<RulePOJO> rulePOJOS);
}
