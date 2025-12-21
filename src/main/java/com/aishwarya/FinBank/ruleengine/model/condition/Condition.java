package com.aishwarya.FinBank.ruleengine.model.condition;


public sealed interface Condition
        permits SimpleCondition, CompositeCondition {
}