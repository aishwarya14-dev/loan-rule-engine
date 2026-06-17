package com.aishwarya.FinBank.ruleengine.loader;

import com.aishwarya.FinBank.model.LoanType;
import com.aishwarya.FinBank.model.Rule;

import java.util.List;

public interface RuleLoader {
     List<Rule> loadRules(LoanType loanType);
}
