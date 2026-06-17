package com.aishwarya.Finbank.ruleengine.action;


import com.aishwarya.Finbank.model.LoanApplication;

import java.util.Map;

public interface RuleAction {
    public void applyAction(LoanApplication application);
}
