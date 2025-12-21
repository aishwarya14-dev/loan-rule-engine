package com.aishwarya.FinBank.ruleengine.factory;


import com.aishwarya.FinBank.ruleengine.rule_evaluation.RuleEvaluation;
import com.aishwarya.FinBank.ruleengine.rule_evaluation.SimpleRuleEvaluation;
import com.aishwarya.FinBank.utility.FieldAccessorRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SimpleRuleEvaluationFactory {
    @Autowired
    public FieldAccessorRegistry registry;

    public RuleEvaluation createSimpleRule(String field, String operator, Object value) {
        return new SimpleRuleEvaluation(field, operator, value, registry);
    }
}
