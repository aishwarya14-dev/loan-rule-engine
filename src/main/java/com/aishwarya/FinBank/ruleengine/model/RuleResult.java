package com.aishwarya.FinBank.ruleengine.model;

import org.springframework.stereotype.Component;

@Component
public class RuleResult {
    private boolean passed;
    private String message;
    private Object expectedValue;

    public RuleResult(boolean passed, String message,Object expectedValue) {
        this.passed = passed;
        this.message = message;
        this.expectedValue = expectedValue;
    }

    public boolean isPassed() {
        return passed;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getExpectedValue() {
        return expectedValue;
    }
}
