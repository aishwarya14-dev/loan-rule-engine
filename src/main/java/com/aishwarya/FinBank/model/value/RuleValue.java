package com.aishwarya.FinBank.model.value;


public sealed interface RuleValue permits DoubleValue, IntValue, StringValue {
    Object returnValue();
}
