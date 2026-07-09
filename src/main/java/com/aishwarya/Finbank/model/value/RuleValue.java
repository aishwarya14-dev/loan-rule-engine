package com.aishwarya.Finbank.model.value;


public sealed interface RuleValue permits DoubleValue, IntValue, StringValue, BooleanValue {
    Object returnValue();
}
