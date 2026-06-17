package com.aishwarya.Finbank.dto.rules.staticrules;


import com.aishwarya.Finbank.model.Action;
import com.aishwarya.Finbank.model.RuleType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class RuleDto {
    private ExpressionDto expression;
    private Action action;
    private RuleType type;

    public RuleDto(ExpressionDto expression, Action action, RuleType type) {
        this.expression = expression;
        this.action = action;
        this.type = type;
    }
}
