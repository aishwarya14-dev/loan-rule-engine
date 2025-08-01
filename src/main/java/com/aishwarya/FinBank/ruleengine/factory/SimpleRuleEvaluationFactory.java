package com.aishwarya.FinBank.ruleengine.factory;

import com.aishwarya.FinBank.ruleengine.evaluation.RuleEvaluation;
import com.aishwarya.FinBank.ruleengine.evaluation.SimpleRuleEvaluation;
import com.aishwarya.FinBank.utility.FieldAccessorRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SimpleRuleEvaluationFactory {
    @Autowired
    private FieldAccessorRegistry registry;


    public RuleEvaluation createSimpleCondition(String field, String operator, Object value) {
        return new SimpleRuleEvaluation(field, operator, value, registry);
    }
}
