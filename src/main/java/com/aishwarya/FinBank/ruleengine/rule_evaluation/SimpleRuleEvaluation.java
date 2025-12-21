package com.aishwarya.FinBank.ruleengine.rule_evaluation;

import com.aishwarya.FinBank.model.LoanApplication;
import com.aishwarya.FinBank.ruleengine.model.RuleMessageGenerator;
import com.aishwarya.FinBank.ruleengine.model.RuleResult;
import com.aishwarya.FinBank.utility.ComparisonEvaluator;
import com.aishwarya.FinBank.utility.FieldAccessorRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@Scope("prototype")
public class SimpleRuleEvaluation implements RuleEvaluation {
    private String field;
    private String operator;
    private Object expectedValue;
    private FieldAccessorRegistry registry;
    @Autowired
    private RuleMessageGenerator messageGenerator;

   public SimpleRuleEvaluation(String field, String operator, Object expectedValue, FieldAccessorRegistry registry) {
        this.field = field;
        this.operator = operator;
        this.expectedValue = expectedValue;
        this.registry = registry;
    }

    @Override
    public RuleResult evaluate(LoanApplication application) {
        Function<LoanApplication,Object> function = registry.getAccessor(field);
        Object actualValue = function.apply(application);

        boolean result = ComparisonEvaluator.evaluate(actualValue, expectedValue,operator);
        String message = messageGenerator.generateMessage(field + " " + expectedValue, result);
        return new RuleResult(result, message, expectedValue);
    }
}
