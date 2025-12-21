package com.aishwarya.FinBank.ruleengine.rule_evaluation;

import com.aishwarya.FinBank.model.LoanApplication;
import com.aishwarya.FinBank.ruleengine.model.RuleResult;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
public class CompositeRuleEvaluation implements RuleEvaluation {
    private String logic;
    private List<RuleEvaluation> ruleEvaluations;

    @Override
    public RuleResult evaluate(LoanApplication application) {
        return switch (logic) {
            case "AND" -> ruleEvaluations.stream().allMatch(c -> c.evaluate(application).isPassed()) ? new RuleResult(true, "", null) : new RuleResult(false, "", null);
            case "OR" -> ruleEvaluations.stream().anyMatch(c -> c.evaluate(application).isPassed()) ? new RuleResult(true, "", null) : new RuleResult(false, "", null);
            default -> null;
        };
    }

    public void setLogic(String logic) {
        this.logic = logic;
    }

    public void setRuleEvaluations(List<RuleEvaluation> ruleEvaluations) {
        this.ruleEvaluations = ruleEvaluations;
    }
}
