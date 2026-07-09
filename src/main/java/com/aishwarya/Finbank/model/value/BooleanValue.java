package com.aishwarya.Finbank.model.value;

public record BooleanValue(boolean value) implements RuleValue {
    public Object returnValue() { return value; }
}
