package com.aishwarya.FinBank.ruleengine.model.value;

public record StringValue(String value) implements RuleValue{
    public Object returnValue() {
        return value;
    }
}
