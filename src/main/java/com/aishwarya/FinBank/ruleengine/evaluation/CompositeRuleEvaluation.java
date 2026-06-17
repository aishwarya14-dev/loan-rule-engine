package com.aishwarya.Finbank.ruleengine.evaluation;

import com.aishwarya.Finbank.model.LoanApplication;

import com.aishwarya.Finbank.utility.LoanFieldAccessorRegistry;
import com.aishwarya.Finbank.model.Logic;
import com.aishwarya.Finbank.model.RuleMessageGenerator;
import com.aishwarya.Finbank.model.RuleResult;

import java.util.List;
import java.util.stream.Collectors;

public class CompositeRuleEvaluation implements RuleEvaluation {
    private Logic logic;
    private List<RuleEvaluation> ruleEvaluations;
    private RuleMessageGenerator messageGenerator;


    public CompositeRuleEvaluation(List<RuleEvaluation> ruleEvaluations, Logic logic, RuleMessageGenerator messageGenerator) {
        this.ruleEvaluations = ruleEvaluations;
        this.logic = logic;
        this.messageGenerator = messageGenerator;
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

        String message = messageGenerator.generateMessage(
                buildMessageSummary(results), finalResult
        );

        return new RuleResult(finalResult, message, String.valueOf(collectExpectedValues(results)), application);
    }

    private String buildMessageSummary(List<RuleResult> results) {
        return results.stream()
                .map(RuleResult::getMessage)
                .collect(Collectors.joining(" " + logic.name() + " "));
    }

    // Collects all expected values
    private List<String> collectExpectedValues(List<RuleResult> results) {
        return results.stream()
                .map(RuleResult::getExpectedValue)
                .toList();
    }
}
