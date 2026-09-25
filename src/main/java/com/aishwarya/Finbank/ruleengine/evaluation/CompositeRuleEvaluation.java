package com.aishwarya.Finbank.ruleengine.evaluation;

import com.aishwarya.Finbank.enums.Action;
import com.aishwarya.Finbank.exceptions.InvalidRuleConfigurationException;
import com.aishwarya.Finbank.exceptions.RuleEvaluationException;
import com.aishwarya.Finbank.metrics.RuleEngineMetrics;
import com.aishwarya.Finbank.model.*;

import com.aishwarya.Finbank.model.expression.AndExpression;
import com.aishwarya.Finbank.model.expression.Condition;
import com.aishwarya.Finbank.model.expression.Expression;
import com.aishwarya.Finbank.model.expression.OrExpression;
import com.aishwarya.Finbank.service.LoanTypeFactorConfigService;
import com.aishwarya.Finbank.utility.ComparisonEvaluator;
import com.aishwarya.Finbank.utility.LoanFieldAccessorRegistry;
import com.aishwarya.Finbank.utility.RuleMessageGenerator;

import java.util.Optional;
import java.util.function.Function;

public class CompositeRuleEvaluation implements RuleEvaluation {
    private final RuleMessageGenerator messageGenerator;
    private final LoanTypeFactorConfigService loanTypeFactorConfigService;
    private final RuleEngineMetrics metrics;
    private final RuleEvaluationHelper ruleEvaluationHelper;

    public CompositeRuleEvaluation(RuleMessageGenerator messageGenerator,LoanTypeFactorConfigService loanTypeFactorConfigService,RuleEngineMetrics metrics,RuleEvaluationHelper ruleEvaluationHelper) {
        this.messageGenerator = messageGenerator;
        this.loanTypeFactorConfigService = loanTypeFactorConfigService;
        this.metrics = metrics;
        this.ruleEvaluationHelper = ruleEvaluationHelper;
    }

    @Override
    public RuleResult evaluate(LoanApplication application,Rule rule) {
        boolean evaluationResult = evaluateExpression(application, rule.getExpression());
        double score = ruleEvaluationHelper.calculateRuleContributionScore(rule,evaluationResult);
        String message = messageGenerator.generateMessage(rule, evaluationResult);
        // update evaluation metrics
        if (evaluationResult || ( !evaluationResult && rule.getAction() == Action.REJECT)) {
            metrics.incrementEvaluationPassed();
        } else {
            metrics.incrementEvaluationFailed();
        }
        metrics.incrementEvaluationTotal();
        RuleResult result =
                new RuleResult(
                        evaluationResult,
                        message,
                        score,
                        application
                );
        result.setLoanTypeFactorConfig(
                loanTypeFactorConfigService.getLoanTypeFactorConfig(
                        application.getLoanType().getId(),
                        rule.getFactorId()
                )
        );
        return result;
    }

    private boolean evaluateExpression(LoanApplication application,Expression expression) {
        if (expression instanceof Condition condition) {
            return evaluateCondition(application, condition);
        }
        else if (expression instanceof AndExpression andExpression) {
            return evaluateExpression(application, andExpression.getLeft())
                    && evaluateExpression(application, andExpression.getRight());
        }
        else if (expression instanceof OrExpression orExpression) {
            return evaluateExpression(application, orExpression.getLeft())
                    || evaluateExpression(application, orExpression.getRight());
        }
        throw new IllegalArgumentException("Unknown expression");
    }

    private boolean evaluateCondition(LoanApplication application,Condition condition) {
        //get the function to fetch the actual value of the field from the application
        Function<LoanApplication, Object> actualValGetterFunction = ruleEvaluationHelper.getActualValGetterFunction(condition.getField());
        //get the actual value of the field from the application
        Object actualValue = ruleEvaluationHelper.getActualValue(actualValGetterFunction,application,condition.getField());
        return ruleEvaluationHelper.compareActualVsExpectedValue(actualValue,condition);
    }
}
