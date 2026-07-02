package com.aishwarya.Finbank.ruleengine.evaluation;

import com.aishwarya.Finbank.exceptions.InvalidRuleConfigurationException;
import com.aishwarya.Finbank.exceptions.RuleEvaluationException;
import com.aishwarya.Finbank.metrics.RuleEngineMetrics;
import com.aishwarya.Finbank.model.*;
import com.aishwarya.Finbank.model.expression.Condition;
import com.aishwarya.Finbank.repository.LoanTypeFactorConfigRepo;
import com.aishwarya.Finbank.service.LoanTypeFactorConfigService;
import com.aishwarya.Finbank.utility.LoanFieldAccessorRegistry;
import com.aishwarya.Finbank.utility.ComparisonEvaluator;

import java.util.Optional;
import java.util.function.Function;

public class SimpleRuleEvaluation implements RuleEvaluation {
    private final LoanFieldAccessorRegistry registry;
    private final RuleMessageGenerator messageGenerator;
    private final Rule rule;
    private final LoanTypeFactorConfigService loanTypeFactorConfigService;
    private final RuleEngineMetrics metrics;

    public SimpleRuleEvaluation(Rule rule, LoanFieldAccessorRegistry registry, RuleMessageGenerator messageGenerator, LoanTypeFactorConfigService loanTypeFactorConfigService,RuleEngineMetrics metrics) {
        this.rule = rule;
        this.registry = registry;
        this.messageGenerator = messageGenerator;
        this.loanTypeFactorConfigService = loanTypeFactorConfigService;
        this.metrics = metrics;
    }

    @Override
    public RuleResult evaluate(LoanApplication application) {
        if (!(rule.getExpression() instanceof Condition))
            throw new InvalidRuleConfigurationException(
                    "Expected Condition expression for rule ");

        Condition condition = (Condition) rule.getExpression();
        //get the function to fetch the actual value of the field from the application
        Function<LoanApplication, Object> actualValGetterFunction = getActualValGetterFunction(condition.getField());
        //get the actual value of the field from the application
        Object actualValue = getActualValue(actualValGetterFunction,application,condition.getField());
        //compare the actual value with the expected value based on the operator
        boolean evaluationResult = compareActualVsExpectedValue(actualValue,condition);
        double score = calculateScore(evaluationResult);
        metrics.incrementEvaluationPassed();
        //generate the message based on the evaluation result
        String message = messageGenerator.generateMessage(condition.getField(), condition.getOperator() + "", condition.getValue(), actualValue, evaluationResult);


        // create and return the RuleResult object
        RuleResult result = new RuleResult(evaluationResult, message, Double.valueOf(score), application);
        LoanTypeFactorConfig loanTypeFactorConfig = loanTypeFactorConfigService.getLoanTypeFactorConfig(application.getLoanType().getId(),rule.getFactorId());
        result.setLoanTypeFactorConfig(loanTypeFactorConfig);
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
            metrics.incrementEvaluationSkipped();
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
            metrics.incrementEvaluationFailed();
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

    private double calculateScore(boolean result){
        double score = 0.0;
        if(result && rule.getAction() == Action.REJECT){
            score = rule.getEvidenceWeight() * -1;
        } else if((result && rule.getAction() == Action.APPROVE) ||
                  !result && rule.getAction() == Action.REJECT){
            score = rule.getEvidenceWeight();
        }
        return score;
    }
}
