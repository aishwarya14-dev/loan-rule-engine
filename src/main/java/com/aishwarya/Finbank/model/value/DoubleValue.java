package com.aishwarya.Finbank.model.value;

public record DoubleValue(double value) implements RuleValue {
    public Object returnValue() {
        return value;
    }
}
