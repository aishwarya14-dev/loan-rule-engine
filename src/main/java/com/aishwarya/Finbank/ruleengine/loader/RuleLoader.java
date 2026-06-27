package com.aishwarya.Finbank.ruleengine.loader;


import com.aishwarya.Finbank.model.LoanType;
import com.aishwarya.Finbank.model.Rule;

import java.util.List;

public interface RuleLoader {
    List<Rule> loadRules(LoanType loanType);
}
