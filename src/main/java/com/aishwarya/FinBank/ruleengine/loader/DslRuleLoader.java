package com.aishwarya.FinBank.ruleengine.loader;

import com.aishwarya.FinBank.ruleengine.model.Rule;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("dslRuleLoader")
public class DslRuleLoader implements RuleLoader{
    @Override
    public List<Rule> loadRules() {
        return List.of();
    }
}
