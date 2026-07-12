package com.aishwarya.Finbank.ruleengine.evaluation;

import com.aishwarya.Finbank.enums.Action;
import com.aishwarya.Finbank.exceptions.InvalidRuleConfigurationException;
import com.aishwarya.Finbank.exceptions.RuleEvaluationException;
import com.aishwarya.Finbank.metrics.RuleEngineMetrics;
import com.aishwarya.Finbank.model.LoanApplication;
import com.aishwarya.Finbank.model.Rule;
import com.aishwarya.Finbank.model.expression.Condition;
import com.aishwarya.Finbank.utility.ComparisonEvaluator;
import com.aishwarya.Finbank.utility.LoanFieldAccessorRegistry;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Function;

@Component
@AllArgsConstructor
public class RuleEvaluationHelper {

    private final LoanFieldAccessorRegistry registry;
    private final RuleEngineMetrics metrics;

    protected Function<LoanApplication, Object> getActualValGetterFunction(String field) {
        return Optional.of(registry.getActualValGetterFunction(field))
                .orElseThrow(() -> {
                    metrics.incrementEvaluationSkipped();
                           return new InvalidRuleConfigurationException(
                                    "Unsupported field: " + field);
                        }
                );
    }

    protected Object getActualValue(Function<LoanApplication, Object> actualValGetterFunction,LoanApplication application,String field){
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

    protected boolean compareActualVsExpectedValue(Object actualValue, Condition condition){
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
                            "Failed evaluating rule Field=%s, Operator=%s,Value=%s",
                            condition.getField(),
                            condition.getOperator(),
                            condition.getValue().toString()
                    )
            );
        }
        return evaluationResult;
    }

    protected double calculateRuleContributionScore(Rule rule, boolean result){
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
