package com.aishwarya.Finbank.ruleengine.evaluation;

import com.aishwarya.Finbank.model.*;

import com.aishwarya.Finbank.repository.LoanTypeFactorConfigRepo;
import com.aishwarya.Finbank.service.LoanTypeFactorConfigService;
import com.aishwarya.Finbank.utility.LoanFieldAccessorRegistry;

import java.util.List;
import java.util.stream.Collectors;

public class CompositeRuleEvaluation implements RuleEvaluation {
    private Logic logic;
    private List<RuleEvaluation> ruleEvaluations;
    private RuleMessageGenerator messageGenerator;
    private Rule rule;
    private LoanTypeFactorConfigService loanTypeFactorConfigService;


    public CompositeRuleEvaluation(List<RuleEvaluation> ruleEvaluations, Logic logic, RuleMessageGenerator messageGenerator,Rule rule,LoanTypeFactorConfigService loanTypeFactorConfigService) {
        this.ruleEvaluations = ruleEvaluations;
        this.logic = logic;
        this.messageGenerator = messageGenerator;
        this.rule = rule;
        this.loanTypeFactorConfigService = loanTypeFactorConfigService;
    }

    @Override
    public RuleResult evaluate(LoanApplication application) {
        List<RuleResult> results = ruleEvaluations.stream()
                .map(evaluation -> evaluation.evaluate(application))
                .toList();

        boolean finalResult = switch (logic) {
            case AND -> results.stream().allMatch(RuleResult::isPassed);
            case OR -> results.stream().anyMatch(RuleResult::isPassed);
        };

        double score = calculateScore(finalResult);
        String message = messageGenerator.generateMessage(
                buildMessageSummary(results), finalResult
        );

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
