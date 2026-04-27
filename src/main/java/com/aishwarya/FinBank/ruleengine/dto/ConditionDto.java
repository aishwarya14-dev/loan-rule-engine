package com.aishwarya.FinBank.ruleengine.dto;

import com.aishwarya.FinBank.ruleengine.model.value.RuleValue;
import com.aishwarya.FinBank.utility.Operator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class ConditionDto implements ExpressionDto {
    private String field;
    private Operator operator;
    private RuleValue value;
}
