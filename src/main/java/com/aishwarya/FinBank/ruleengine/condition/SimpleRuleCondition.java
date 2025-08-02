package com.aishwarya.FinBank.ruleengine.condition;

import com.aishwarya.FinBank.model.LoanApplication;
import com.aishwarya.FinBank.utility.ComparisonEvaluator;
import com.aishwarya.FinBank.utility.FieldAccessorRegistry;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component("simpleRuleCondition")
public class SimpleRuleCondition implements RuleCondition {
    private String field;
    private String operator;
    private Object expectedValue;
    private FieldAccessorRegistry registry;

    public SimpleRuleCondition(String field, String operator, Object value, FieldAccessorRegistry registry) {
        this.field = field;
        this.operator = operator;
        this.expectedValue = value;
        this.registry = registry;
    }

    @Override
    public boolean evaluate(LoanApplication application) {
        Function<LoanApplication,Object> function = registry.getAccessor(field);
        Object actualValue = function.apply(application);

        return ComparisonEvaluator.evaluate(actualValue, expectedValue,operator);
    }
}
