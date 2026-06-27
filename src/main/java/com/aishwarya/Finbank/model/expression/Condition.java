package com.aishwarya.Finbank.model.expression;


import com.aishwarya.FinBank.utility.Operator;
import com.aishwarya.Finbank.model.value.RuleValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Condition implements Expression, Serializable {
    private String field;
    private Operator operator;
    private RuleValue value;
}