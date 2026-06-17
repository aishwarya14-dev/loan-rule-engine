package com.aishwarya.Finbank.ruleengine.evaluation;

import com.aishwarya.Finbank.model.LoanApplication;
import com.aishwarya.Finbank.model.RuleResult;


public interface RuleEvaluation {
    RuleResult evaluate(LoanApplication application);
}
