package com.aishwarya.FinBank.ruleengine.model.value;

public record DoubleValue(double value) implements RuleValue {
    public Object returnValue() {
        return value;
    }
}
