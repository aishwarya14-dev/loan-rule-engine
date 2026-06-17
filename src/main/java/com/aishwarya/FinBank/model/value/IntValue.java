package com.aishwarya.Finbank.model.value;

public record IntValue(int value) implements RuleValue {
    public Object returnValue() {
        return value;
    }
}
