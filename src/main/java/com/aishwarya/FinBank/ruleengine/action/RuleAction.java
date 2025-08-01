package com.aishwarya.FinBank.ruleengine.action;


import com.aishwarya.FinBank.model.LoanApplication;

import java.util.Map;

public interface RuleAction {
     public void applyAction(LoanApplication application);
}
