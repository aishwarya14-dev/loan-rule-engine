package com.aishwarya.FinBank.ruleengine.rule_evaluation;

import com.aishwarya.FinBank.model.LoanApplication;
import com.aishwarya.FinBank.ruleengine.model.RuleResult;

public interface RuleEvaluation {
    RuleResult evaluate(LoanApplication application);
}
