package com.aishwarya.FinBank.ruleengine.condition;

import com.aishwarya.FinBank.model.LoanApplication;

public interface RuleCondition {
    boolean evaluate(LoanApplication application);
}
