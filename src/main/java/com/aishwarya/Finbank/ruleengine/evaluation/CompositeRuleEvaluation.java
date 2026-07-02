package com.aishwarya.Finbank.ruleengine.evaluation;

import com.aishwarya.Finbank.metrics.RuleEngineMetrics;
import com.aishwarya.Finbank.model.*;

import com.aishwarya.Finbank.repository.LoanTypeFactorConfigRepo;
import com.aishwarya.Finbank.service.LoanTypeFactorConfigService;
import com.aishwarya.Finbank.utility.LoanFieldAccessorRegistry;

import java.util.List;
import java.util.stream.Collectors;

public class CompositeRuleEvaluation implements RuleEvaluation {
    private final Logic logic;
    private final List<RuleEvaluation> ruleEvaluations;
    private final RuleMessageGenerator messageGenerator;
    private final Rule rule;
    private final LoanTypeFactorConfigService loanTypeFactorConfigService;
    private final RuleEngineMetrics metrics;


    public CompositeRuleEvaluation(List<RuleEvaluation> ruleEvaluations, Logic logic, RuleMessageGenerator messageGenerator,Rule rule,LoanTypeFactorConfigService loanTypeFactorConfigService,RuleEngineMetrics metrics) {
        this.ruleEvaluations = ruleEvaluations;
        this.logic = logic;
        this.messageGenerator = messageGenerator;
        this.rule = rule;
        this.loanTypeFactorConfigService = loanTypeFactorConfigService;
        this.metrics = metrics;
    }

    @Override
    public RuleResult evaluate(LoanApplication application) {

        // evaluate each rule in the composite rule and collect the results
        List<RuleResult> results = ruleEvaluations.stream()
                .map(evaluation -> evaluation.evaluate(application))
                .toList();

        // combine the results based on the specified logic (AND/OR)
        boolean finalResult = switch (logic) {
            case AND -> results.stream().allMatch(RuleResult::isPassed);
            case OR -> results.stream().anyMatch(RuleResult::isPassed);
        };

        // calculate the score based on the final result and the rule's action
        double score = calculateScore(finalResult);
        String message = messageGenerator.generateMessage(
                buildMessageSummary(results), finalResult
        );
        metrics.incrementEvaluationPassed();

        // create and return the RuleResult object
        RuleResult result = new RuleResult(finalResult, message, score, application);
        LoanTypeFactorConfig loanTypeFactorConfig = loanTypeFactorConfigService.getLoanTypeFactorConfig(application.getLoanType().getId(),rule.getFactorId());
        result.setLoanTypeFactorConfig(loanTypeFactorConfig);
        return result;
    }

    private String buildMessageSummary(List<RuleResult> results) {
        return results.stream()
                .map(RuleResult::getMessage)
                .collect(Collectors.joining(" " + logic.name() + " "));
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
