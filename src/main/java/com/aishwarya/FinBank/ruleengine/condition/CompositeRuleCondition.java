package com.aishwarya.FinBank.ruleengine.condition;

import com.aishwarya.FinBank.model.LoanApplication;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("compositeRuleCondition")
public class CompositeRuleCondition implements RuleCondition {
    private String logic;
    private List<RuleCondition> ruleConditions;

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

    public void setRuleConditions(List<RuleCondition> ruleConditions) {
        this.ruleConditions = ruleConditions;
    }
}
