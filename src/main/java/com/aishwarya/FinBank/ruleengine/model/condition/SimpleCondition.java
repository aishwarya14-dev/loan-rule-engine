package com.aishwarya.FinBank.ruleengine.model.condition;

public final class SimpleCondition implements Condition {
    private final String field;
    private final String operator;
    private final Object value;

    public SimpleCondition(String field, String operator, Object value) {
        this.field = field;
        this.operator = operator;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public String getOperator() {
        return operator;
    }

    public Object getValue() {
        return value;
    }
}
