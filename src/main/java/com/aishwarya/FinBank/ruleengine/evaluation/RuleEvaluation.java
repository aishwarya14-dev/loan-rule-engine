package com.aishwarya.FinBank.ruleengine.evaluation;

import com.aishwarya.FinBank.model.LoanApplication;
import com.aishwarya.FinBank.model.RuleResult;

public interface RuleEvaluation {
    RuleResult evaluate(LoanApplication application);
}
