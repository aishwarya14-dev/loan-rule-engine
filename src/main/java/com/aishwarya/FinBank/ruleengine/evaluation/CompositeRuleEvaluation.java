package com.aishwarya.FinBank.ruleengine.evaluation;

import com.aishwarya.FinBank.model.LoanApplication;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("compositeRuleCondition")
public class CompositeRuleEvaluation implements RuleEvaluation {
    private String logic;
    private List<RuleEvaluation> ruleConditions;

    @Override
    public boolean evaluate(LoanApplication application) {
        return switch (logic) {
            case "AND" -> ruleConditions.stream().allMatch(c -> c.evaluate(application));
            case "OR" -> ruleConditions.stream().anyMatch(c -> c.evaluate(application));
            default -> false;
        };
    }

    public void setLogic(String logic) {
        this.logic = logic;
    }

    public void setRuleConditions(List<RuleEvaluation> ruleConditions) {
        this.ruleConditions = ruleConditions;
    }
}
