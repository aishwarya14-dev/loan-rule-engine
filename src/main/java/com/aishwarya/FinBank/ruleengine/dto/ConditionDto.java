package com.aishwarya.FinBank.ruleengine.dto;

import com.aishwarya.FinBank.utility.Operator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ConditionDto implements ExpressionDto {
    private String field;
    private Operator operator;
    private int value;

    public ConditionDto(String field, Operator operator, int value) {
        this.field = field;
        this.operator = operator;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public Operator getOperator() {
        return operator;
    }

    public int getValue() {
        return value;
    }
}
