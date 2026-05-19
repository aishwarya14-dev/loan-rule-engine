package com.aishwarya.FinBank.model.expression;
import com.aishwarya.FinBank.model.value.RuleValue;
import com.aishwarya.FinBank.utility.Operator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Condition implements Expression , Serializable {
    private String field;
    private Operator operator;
    private RuleValue value;
}