package com.aishwarya.FinBank.utility;

import org.springframework.stereotype.Component;

@Component
public class ComparisonEvaluator {

    public static boolean evaluate(Object fieldValue, Object expectedValue,String operator){
        if(fieldValue instanceof  Number && expectedValue instanceof Number){
            double actualVal = ((Number) fieldValue).doubleValue();
            double expectedVal = ((Number) expectedValue).doubleValue();
            return evaluateNumeric(actualVal,expectedVal,operator);
        }
        else{
            return evaluateObject(fieldValue,expectedValue,operator);
        }
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

    public static boolean evaluateObject(Object actual, Object expected,String operator){
        return switch (operator) {
            case "==" -> actual.equals(expected);
            case "!=" -> !actual.equals(expected);
            default -> false;
        };
     }

}