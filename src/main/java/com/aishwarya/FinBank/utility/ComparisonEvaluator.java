package com.aishwarya.FinBank.utility;

import com.aishwarya.FinBank.ruleengine.model.value.DoubleValue;
import com.aishwarya.FinBank.ruleengine.model.value.IntValue;
import com.aishwarya.FinBank.ruleengine.model.value.RuleValue;
import com.aishwarya.FinBank.ruleengine.model.value.StringValue;
import org.springframework.stereotype.Component;

@Component
public class ComparisonEvaluator {

    public static boolean evaluate(Object actualValue, RuleValue expectedValue, Operator operator){
        return switch (expectedValue) {
            case IntValue iv -> evaluateNumeric(((Number) actualValue).intValue(), iv.value(), operator.getSymbol());
            case StringValue sv -> evaluateString(String.valueOf(actualValue), sv.value(), operator.getSymbol());
            case DoubleValue dv -> evaluateNumeric(((Number) actualValue).doubleValue(), dv.value(), operator.getSymbol());
            default -> throw new IllegalArgumentException("Unsupported RuleValue type: " + expectedValue.getClass().getName());
        };
    }

    public static boolean evaluateNumeric(double actual, double expected,String operator){
        return switch (operator){
            case ">" -> actual > expected;
            case ">=" -> actual >= expected;
            case "<" -> actual < expected;
            case "<=" -> actual <= expected;
            case "==" -> actual == expected;
            case "!=" -> actual != expected;
            default -> false;
        };
    }

    public static boolean evaluateString(String actual, String expected,String operator){
        return switch (operator){
            case ">" -> actual.compareTo(expected) > 0;
            case "<" -> actual.compareTo(expected) < 0;
            case "==" -> actual == expected;
            case "!=" -> actual != expected;
            default -> false;
        };
    }

    public static boolean evaluateObject(Object actual, Object expected,String operator){
        return switch (operator) {
            case "==" -> actual.equals(expected);
            case "!=" -> !actual.equals(expected);
            default -> false;
        };
     }

}