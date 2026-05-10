package com.aishwarya.FinBank.model.expression;
import com.aishwarya.FinBank.model.value.RuleValue;
import com.aishwarya.FinBank.utility.Operator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Condition implements Expression {
    private String field;
    private Operator operator;
    private RuleValue value;
}