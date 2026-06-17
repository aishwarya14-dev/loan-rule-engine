package com.aishwarya.FinBank.ruleengine.evaluation;

import com.aishwarya.FinBank.model.LoanApplication;
import com.aishwarya.FinBank.model.RuleMessageGenerator;
import com.aishwarya.FinBank.model.RuleResult;
import com.aishwarya.FinBank.model.value.RuleValue;
import com.aishwarya.FinBank.utility.ComparisonEvaluator;
import com.aishwarya.FinBank.utility.LoanFieldAccessorRegistry;
import com.aishwarya.FinBank.utility.Operator;

import java.util.function.Function;

public class SimpleRuleEvaluation implements RuleEvaluation {
    private String field;
    private Operator operator;
    private RuleValue expectedValue;
    private LoanFieldAccessorRegistry registry;
    private RuleMessageGenerator messageGenerator;

   public SimpleRuleEvaluation(String field, Operator operator, RuleValue expectedValue, LoanFieldAccessorRegistry registry,RuleMessageGenerator messageGenerator) {
        this.field = field;
        this.operator = operator;
        this.expectedValue = expectedValue;
        this.registry = registry;
        this.messageGenerator = messageGenerator;
    }

    @Override
    public RuleResult evaluate(LoanApplication application) {
        Function<LoanApplication,Object> actualValGetterFunction = registry.getActualValGetterFunction(field);
        //get the actual value of the field from the application
        Object actualValue = actualValGetterFunction.apply(application);

        boolean result = ComparisonEvaluator.evaluate(actualValue, expectedValue,operator);
        String message = messageGenerator.generateMessage(field, operator + "",  expectedValue,actualValue, result);
        return new RuleResult(result, message, String.valueOf(expectedValue), application);
    }
}
