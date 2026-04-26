package com.aishwarya.FinBank.ruleengine.loader;

import com.aishwarya.FinBank.ruleengine.model.RulePOJO;

import java.util.List;

public interface RuleLoader {
     List<RulePOJO> loadRules();
}
