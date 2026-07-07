package com.aishwarya.Finbank.utility;

import org.springframework.stereotype.Component;

@Component
public class RuleMessageGenerator {

    public String generateMessage(String field, String operator,
                                  Object expectedValue, Object actualValue,
                                  boolean passed) {
        String rule = field + " " + operator + " " + expectedValue;
        if (passed) {
            return String.format("Rule '%s' passed ", rule);
        } else {
            return String.format(
                    "Rule '%s' failed (Expected: %s %s %s, Actual: %s) ",
                    rule, field, operator, expectedValue, actualValue
            );
        }
    }

    // Overload for composite rules (AND/OR summary)
    public String generateMessage(String compositeSummary, boolean passed) {
        if (passed) {
            return String.format("Composite rule '%s' passed ", compositeSummary);
        } else {
            return String.format("Composite rule '%s' failed ", compositeSummary);
        }
    }
}

