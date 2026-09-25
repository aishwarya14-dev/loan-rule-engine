package com.aishwarya.Finbank.ruleengine.evaluation;

import com.aishwarya.Finbank.enums.Action;
import com.aishwarya.Finbank.exceptions.InvalidRuleConfigurationException;
import com.aishwarya.Finbank.metrics.RuleEngineMetrics;
import com.aishwarya.Finbank.model.*;
import com.aishwarya.Finbank.model.expression.Condition;
import com.aishwarya.Finbank.service.LoanTypeFactorConfigService;
import com.aishwarya.Finbank.utility.LoanFieldAccessorRegistry;
import com.aishwarya.Finbank.utility.RuleMessageGenerator;

import java.util.function.Function;

public class SimpleRuleEvaluation implements RuleEvaluation {
    private final RuleMessageGenerator messageGenerator;
    private final LoanTypeFactorConfigService loanTypeFactorConfigService;
    private final RuleEngineMetrics metrics;
    private final RuleEvaluationHelper ruleEvaluationHelper;

    public SimpleRuleEvaluation(RuleMessageGenerator messageGenerator,LoanTypeFactorConfigService loanTypeFactorConfigService,RuleEngineMetrics metrics,RuleEvaluationHelper ruleEvaluationHelper){
        this.messageGenerator = messageGenerator;
        this.loanTypeFactorConfigService = loanTypeFactorConfigService;
        this.metrics = metrics;
        this.ruleEvaluationHelper = ruleEvaluationHelper;
    }

    @Override
    public RuleResult evaluate(LoanApplication application,Rule rule) {
        if (!(rule.getExpression() instanceof Condition condition))
            throw new InvalidRuleConfigurationException(
                    "Expected Condition expression for rule ");

        //get the function to fetch the actual value of the field from the application
        Function<LoanApplication, Object> actualValGetterFunction = ruleEvaluationHelper.getActualValGetterFunction(condition.getField());
        //get the actual value of the field from the application
        Object actualValue = ruleEvaluationHelper.getActualValue(actualValGetterFunction,application,condition.getField());
        //compare the actual value with the expected value based on the operator
        boolean evaluationResult = ruleEvaluationHelper.compareActualVsExpectedValue(actualValue,condition);
        double ruleContributionScore = ruleEvaluationHelper.calculateRuleContributionScore(rule,evaluationResult);
        // update evaluation metrics
        if (evaluationResult || ( !evaluationResult && rule.getAction() == Action.REJECT)) {
            metrics.incrementEvaluationPassed();
        } else {
            metrics.incrementEvaluationFailed();
        }
        metrics.incrementEvaluationTotal();
        //generate the message based on the evaluation result
        String message = messageGenerator.generateMessage(condition.getField(), condition.getOperator().toString(), condition.getValue(), actualValue, evaluationResult);


        // create and return the RuleResult object
        RuleResult result = new RuleResult(evaluationResult, message, ruleContributionScore, application);
        LoanTypeFactorConfig loanTypeFactorConfig = loanTypeFactorConfigService.getLoanTypeFactorConfig(application.getLoanType().getId(),rule.getFactorId());
        result.setLoanTypeFactorConfig(loanTypeFactorConfig);
        return result;
    }
}
