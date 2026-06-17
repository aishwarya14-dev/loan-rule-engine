package com.aishwarya.Finbank.dto.rules.staticrules;


import com.aishwarya.FinBank.utility.Operator;
import com.aishwarya.Finbank.model.value.RuleValue;
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
