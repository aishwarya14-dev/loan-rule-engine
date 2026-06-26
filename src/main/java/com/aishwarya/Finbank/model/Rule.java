package com.aishwarya.Finbank.model;

import com.aishwarya.Finbank.enums.RuleSeverity;
import com.aishwarya.Finbank.model.expression.Expression;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Rule {
    private Expression expression;
    private Action action;
    private RuleType type;
    private Double evidenceWeight;
    private Integer importanceLevel;
    private Long factorId;
    private Long loanTypeId;
    private RuleSeverity severity = RuleSeverity.NORMAL;

    public Rule(Expression expression, Action action, RuleType type) {
        this.expression = expression;
        this.action = action;
        this.type = type;
    }
}

