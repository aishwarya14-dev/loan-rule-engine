package com.aishwarya.FinBank.ruleengine.model;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
public class Condition {
    public String operator;
    public Object value;
    public String field;
    private List<Object> conditions;  // can be of type Rule or Condition


    public Object getValue() {
        return value;
    }

    public String getOperator() {
        return operator;
    }

    public String getField() {
        return field;
    }

    public List<Object> getConditions() {
        return conditions;
    }

}
