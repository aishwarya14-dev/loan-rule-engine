package com.aishwarya.Finbank.dto.rules.staticrules;


import com.aishwarya.Finbank.enums.RuleSeverity;
import com.aishwarya.Finbank.model.Action;
import com.aishwarya.Finbank.model.Factor;
import com.aishwarya.Finbank.model.RuleType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class StaticRuleDto {
    private ExpressionDto expression;
    private Action action;
    private RuleType type;
    private Double evidenceWeight;
    @Enumerated(EnumType.STRING)
    private RuleSeverity severity = RuleSeverity.NORMAL;
    private Factor factor;

    public StaticRuleDto(ExpressionDto expression, Action action, RuleType type, Double evidenceWeight, RuleSeverity severity,Factor factor) {
        this.expression = expression;
        this.action = action;
        this.type = type;
        this.evidenceWeight = evidenceWeight;
        this.severity = severity;
        this.factor = factor;
    }
}
