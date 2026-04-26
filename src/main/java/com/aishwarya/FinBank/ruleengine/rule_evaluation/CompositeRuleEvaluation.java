package com.aishwarya.FinBank.ruleengine.rule_evaluation;

import com.aishwarya.FinBank.model.LoanApplication;
import com.aishwarya.FinBank.ruleengine.model.Logic;
import com.aishwarya.FinBank.ruleengine.model.RuleMessageGenerator;
import com.aishwarya.FinBank.ruleengine.model.RuleResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

public class CompositeRuleEvaluation implements RuleEvaluation {
    private Logic logic;
    private List<RuleEvaluation> ruleEvaluations;
    @Autowired
    private RuleMessageGenerator ruleMessageGenerator;

    public CompositeRuleEvaluation(List<RuleEvaluation> ruleEvaluations, Logic logic) {
        this.ruleEvaluations = ruleEvaluations;
        this.logic = logic;
    }

    @Override
    public RuleResult evaluate(LoanApplication application) {
        List<RuleResult> results = ruleEvaluations.stream()
                .map(evaluation -> evaluation.evaluate(application))
                .toList();

        boolean finalResult = switch (logic) {
            case AND -> results.stream().allMatch(RuleResult::isPassed);
            case OR  -> results.stream().anyMatch(RuleResult::isPassed);
        };

        String message = ruleMessageGenerator.generateMessage(
                buildMessageSummary(results), finalResult
        );

        return new RuleResult(finalResult, message, collectExpectedValues(results),application);
    }

    private String buildMessageSummary(List<RuleResult> results) {
        return results.stream()
                .map(RuleResult::getMessage)
                .collect(Collectors.joining(" " + logic.name() + " "));
    }

    // Collects all expected values
    private List<Object> collectExpectedValues(List<RuleResult> results) {
        return results.stream()
                .map(RuleResult::getExpectedValue)
                .toList();
    }
}
