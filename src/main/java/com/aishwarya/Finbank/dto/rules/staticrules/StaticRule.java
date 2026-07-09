package com.aishwarya.Finbank.dto.rules.staticrules;


import com.aishwarya.Finbank.enums.Action;
import com.aishwarya.Finbank.enums.RuleType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class StaticRule {
    private ExpressionDto expression;
    private Action action;
    private RuleType type;
    private Double evidenceWeight;

    public StaticRule(ExpressionDto expression, Action action, RuleType type, Double evidenceWeight) {
        this.expression = expression;
        this.action = action;
        this.type = type;
        this.evidenceWeight = evidenceWeight;
    }
}
