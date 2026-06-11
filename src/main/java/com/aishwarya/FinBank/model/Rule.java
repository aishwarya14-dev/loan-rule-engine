package com.aishwarya.FinBank.model;
import com.aishwarya.FinBank.model.expression.Expression;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
public class Rule {
    private Expression expression;
    private Action action;
    private RuleType type;

    public Rule(Expression expression, Action action, RuleType type) {
        this.expression = expression;
        this.action = action;
        this.type = type;
    }
}

