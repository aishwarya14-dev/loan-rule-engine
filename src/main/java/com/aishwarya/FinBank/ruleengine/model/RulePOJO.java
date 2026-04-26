package com.aishwarya.FinBank.ruleengine.model;
import com.aishwarya.FinBank.ruleengine.model.condition.Expression;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RulePOJO {
    private Expression expression;
    private final Action action;
    private final RuleType type;

    public RulePOJO(Expression expression, Action action, RuleType type) {
        this.expression = expression;
        this.action = action;
        this.type = type;
    }
}

