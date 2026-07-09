package com.aishwarya.Finbank.ruleengine.evaluator;

import com.aishwarya.Finbank.model.LoanApplication;
import com.aishwarya.Finbank.model.Rule;


import java.util.List;

public interface RulesEvaluator<T> {
    public T evaluateRules(LoanApplication application, List<Rule> rules);
}
