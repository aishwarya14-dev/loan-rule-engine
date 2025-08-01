package com.aishwarya.FinBank.ruleengine.evaluation;

import com.aishwarya.FinBank.model.LoanApplication;

public interface RuleEvaluation {
    boolean evaluate(LoanApplication application);
}
