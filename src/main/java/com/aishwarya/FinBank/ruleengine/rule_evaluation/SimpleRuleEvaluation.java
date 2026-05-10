package com.aishwarya.FinBank.ruleengine.rule_evaluation;

import com.aishwarya.FinBank.model.LoanApplication;
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

   public SimpleRuleEvaluation(String field, Operator operator, RuleValue expectedValue, LoanFieldAccessorRegistry registry) {
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
//      String message = messageGenerator.generateMessage(field + " " + expectedValue, result);
        return new RuleResult(result, "", String.valueOf(expectedValue), application);
    }
}
