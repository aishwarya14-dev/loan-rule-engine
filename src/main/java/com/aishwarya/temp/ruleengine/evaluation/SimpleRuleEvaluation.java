package com.aishwarya.Finbank.ruleengine.evaluation;

import com.aishwarya.Finbank.model.LoanApplication;
import com.aishwarya.Finbank.utility.LoanFieldAccessorRegistry;
import com.aishwarya.FinBank.utility.Operator;
import com.aishwarya.Finbank.model.RuleMessageGenerator;
import com.aishwarya.Finbank.model.RuleResult;
import com.aishwarya.Finbank.model.value.RuleValue;
import com.aishwarya.Finbank.utility.ComparisonEvaluator;

import java.util.function.Function;

public class SimpleRuleEvaluation implements RuleEvaluation {
    private String field;
    private Operator operator;
    private RuleValue expectedValue;
    private LoanFieldAccessorRegistry registry;
    private RuleMessageGenerator messageGenerator;

    public SimpleRuleEvaluation(String field, Operator operator, RuleValue expectedValue, LoanFieldAccessorRegistry registry, RuleMessageGenerator messageGenerator) {
        this.field = field;
        this.operator = operator;
        this.expectedValue = expectedValue;
        this.registry = registry;
        this.messageGenerator = messageGenerator;
    }

    @Override
    public RuleResult evaluate(LoanApplication application) {
        Function<LoanApplication, Object> actualValGetterFunction = registry.getActualValGetterFunction(field);
        //get the actual value of the field from the application
        Object actualValue = actualValGetterFunction.apply(application);

        boolean result = ComparisonEvaluator.evaluate(actualValue, expectedValue, operator);
        String message = messageGenerator.generateMessage(field, operator + "", expectedValue, actualValue, result);
        return new RuleResult(result, message, String.valueOf(expectedValue), application);
    }
}
