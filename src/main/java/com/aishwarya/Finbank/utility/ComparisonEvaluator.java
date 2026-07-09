package com.aishwarya.Finbank.utility;
import com.aishwarya.FinBank.utility.Operator;
import com.aishwarya.Finbank.model.value.*;
import org.springframework.stereotype.Component;

@Component
public class ComparisonEvaluator {

    public static boolean evaluate(Object actualValue, RuleValue expectedValue, Operator operator) {
        return switch (expectedValue) {
            case IntValue intValue -> evaluateNumeric(((Number) actualValue).intValue(), intValue.value(), operator.getSymbol());
            case StringValue stringValue -> evaluateString(String.valueOf(actualValue), stringValue.value(), operator.getSymbol());
            case DoubleValue doubleValue ->
                    evaluateNumeric(((Number) actualValue).doubleValue(), doubleValue.value(), operator.getSymbol());
            case BooleanValue booleanValue ->
                    evaluateBoolean(Boolean.parseBoolean(actualValue.toString()), booleanValue.value(), operator.getSymbol());
            default ->
                    throw new IllegalArgumentException("Unsupported RuleValue type: " + expectedValue.getClass().getName());
        };
    }

    public static boolean evaluateNumeric(double actual, double expected, String operator) {
        return switch (operator) {
            case ">" -> actual > expected;
            case ">=" -> actual >= expected;
            case "<" -> actual < expected;
            case "<=" -> actual <= expected;
            case "==" -> actual == expected;
            case "!=" -> actual != expected;
            default -> false;
        };
    }

    public static boolean evaluateString(String actual, String expected, String operator) {
        return switch (operator) {
            case ">" -> actual.compareTo(expected) > 0;
            case "<" -> actual.compareTo(expected) < 0;
            case "==" -> actual.equals(expected);
            case "!=" -> !actual.equals(expected);
            default -> false;
        };
    }

    public static boolean evaluateObject(Object actual, Object expected, String operator) {
        return switch (operator) {
            case "==" -> actual.equals(expected);
            case "!=" -> !actual.equals(expected);
            default -> false;
        };
    }

    public static boolean evaluateBoolean(Object actual, Object expected, String operator){
        return switch (operator){
            case "==" -> actual == expected;
            case "!=" -> actual != expected;
            default -> false;
        };
    }

}