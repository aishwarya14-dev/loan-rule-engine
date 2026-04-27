package com.aishwarya.FinBank.ruleengine.factory;


import com.aishwarya.FinBank.ruleengine.model.value.RuleValue;
import com.aishwarya.FinBank.ruleengine.rule_evaluation.RuleEvaluation;
import com.aishwarya.FinBank.ruleengine.rule_evaluation.SimpleRuleEvaluation;
import com.aishwarya.FinBank.utility.LoanFieldAccessorRegistry;
import com.aishwarya.FinBank.utility.Operator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SimpleRuleEvaluationFactory {
    @Autowired
    public LoanFieldAccessorRegistry registry;

    public RuleEvaluation createSimpleRule(String field, Operator operator, RuleValue value) {
        return new SimpleRuleEvaluation(field, operator, value, registry);
    }
}
