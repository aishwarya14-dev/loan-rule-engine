package com.aishwarya.FinBank.ruleengine.factory;

import com.aishwarya.FinBank.ruleengine.condition.RuleCondition;
import com.aishwarya.FinBank.ruleengine.condition.SimpleRuleCondition;
import com.aishwarya.FinBank.utility.FieldAccessorRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SimpleRuleFactory {
    @Autowired
    private FieldAccessorRegistry registry;


    public RuleCondition createSimpleCondition(String field, String operator, Object value) {
        return new SimpleRuleCondition(field, operator, value, registry);
    }
}
