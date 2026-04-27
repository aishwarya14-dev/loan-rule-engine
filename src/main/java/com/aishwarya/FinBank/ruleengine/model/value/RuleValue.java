package com.aishwarya.FinBank.ruleengine.model.value;


public sealed interface RuleValue permits DoubleValue, IntValue, StringValue {
    Object returnValue();
}
