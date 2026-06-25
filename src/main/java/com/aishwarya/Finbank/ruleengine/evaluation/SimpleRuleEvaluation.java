package com.aishwarya.Finbank.ruleengine.evaluation;

import com.aishwarya.Finbank.exceptions.InvalidRuleConfigurationException;
import com.aishwarya.Finbank.exceptions.RuleEvaluationException;
import com.aishwarya.Finbank.model.*;
import com.aishwarya.Finbank.model.expression.Condition;
import com.aishwarya.Finbank.utility.LoanFieldAccessorRegistry;
import com.aishwarya.Finbank.utility.ComparisonEvaluator;

import java.util.Optional;
import java.util.function.Function;

public class SimpleRuleEvaluation implements RuleEvaluation {
    private LoanFieldAccessorRegistry registry;
    private RuleMessageGenerator messageGenerator;
    private Rule rule;

    public SimpleRuleEvaluation(Rule rule, LoanFieldAccessorRegistry registry, RuleMessageGenerator messageGenerator) {
        this.rule = rule;
        this.registry = registry;
        this.messageGenerator = messageGenerator;
    }

    @Override
    public RuleResult evaluate(LoanApplication application) {
        if (!(rule.getExpression() instanceof Condition)) {
            throw new InvalidRuleConfigurationException(
                    "Expected Condition expression for rule ");
        }

        Condition condition = (Condition) rule.getExpression();

        Function<LoanApplication, Object> actualValGetterFunction = getActualValGetterFunction(condition.getField());

        //get the actual value of the field from the application
        Object actualValue = getActualValue(actualValGetterFunction,application,condition.getField());

        boolean evaluationResult = compareActualVsExpectedValue(actualValue,condition);
        double score = 0.0;
        if(evaluationResult)
          score = calculateScore();

        String message = messageGenerator.generateMessage(condition.getField(), condition.getOperator() + "", condition.getValue(), actualValue, evaluationResult);
        RuleResult result = new RuleResult(evaluationResult, message, Double.valueOf(score), application);
        result.setLoanTypeFactorConfig(rule.getLoanTypeFactorConfig());
        return result;
    }

    private Function<LoanApplication, Object> getActualValGetterFunction(String field) {
        return Optional.ofNullable(registry.getActualValGetterFunction(field))
                .orElseThrow(() ->
                        new InvalidRuleConfigurationException(
                                "Unsupported field: " + field));
    }

    private Object getActualValue(Function<LoanApplication, Object> actualValGetterFunction,LoanApplication application,String field){
        Object actualValue;
        try {
            actualValue = actualValGetterFunction.apply(application);
        } catch (Exception ex) {
            throw new RuleEvaluationException(
                    "Failed to fetch value for field: " + field);
        }
        return actualValue;
    }

    private boolean compareActualVsExpectedValue(Object actualValue, Condition condition){
        boolean evaluationResult;
        try {
            evaluationResult = ComparisonEvaluator.evaluate(
                    actualValue,
                    condition.getValue(),
                    condition.getOperator());
        } catch (Exception ex) {
            throw new RuleEvaluationException(
                    String.format(
                            "Failed evaluating rule [%s]. Field=%s, Operator=%s",
                            rule.getExpression(),
                            condition.getField(),
                            condition.getOperator())
                    );
        }
        return evaluationResult;
    }

    private double calculateScore(){
        double score = 0.0;
        if(rule.getAction() == Action.REJECT){
            score = rule.getEvidenceWeight() * -1;
        }else if( rule.getAction() == Action.APPROVE){
            score = rule.getEvidenceWeight();
        }
        return score;
    }
}
