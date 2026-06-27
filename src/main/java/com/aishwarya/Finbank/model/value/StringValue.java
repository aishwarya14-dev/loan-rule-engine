package com.aishwarya.Finbank.model.value;

public record StringValue(String value) implements RuleValue {
    public Object returnValue() {
        return value;
    }
}
