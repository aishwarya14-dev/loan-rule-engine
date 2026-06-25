package com.aishwarya.Finbank.ruleengine.evaluation;

import com.aishwarya.Finbank.model.*;

import com.aishwarya.Finbank.utility.LoanFieldAccessorRegistry;

import java.util.List;
import java.util.stream.Collectors;

public class CompositeRuleEvaluation implements RuleEvaluation {
    private Logic logic;
    private List<RuleEvaluation> ruleEvaluations;
    private RuleMessageGenerator messageGenerator;
    private Rule rule;


    public CompositeRuleEvaluation(List<RuleEvaluation> ruleEvaluations, Logic logic, RuleMessageGenerator messageGenerator,Rule rule) {
        this.ruleEvaluations = ruleEvaluations;
        this.logic = logic;
        this.messageGenerator = messageGenerator;
        this.rule = rule;
    }

    @Override
    public RuleResult evaluate(LoanApplication application) {
        double score = 0.0;
        List<RuleResult> results = ruleEvaluations.stream()
                .map(evaluation -> evaluation.evaluate(application))
                .toList();

        boolean finalResult = switch (logic) {
            case AND -> results.stream().allMatch(RuleResult::isPassed);
            case OR -> results.stream().anyMatch(RuleResult::isPassed);
        };

        if(finalResult && rule.getAction() == Action.REJECT){
            score = rule.getEvidenceWeight() * -1;
        }else if(finalResult && rule.getAction() == Action.APPROVE){
            score = rule.getEvidenceWeight();
        }
        String message = messageGenerator.generateMessage(
                buildMessageSummary(results), finalResult
        );

        RuleResult result = new RuleResult(finalResult, message, score, application);
        result.setLoanTypeFactorConfig(rule.getLoanTypeFactorConfig());
        return result;
    }

    private String buildMessageSummary(List<RuleResult> results) {
        return results.stream()
                .map(RuleResult::getMessage)
                .collect(Collectors.joining(" " + logic.name() + " "));
    }
}
