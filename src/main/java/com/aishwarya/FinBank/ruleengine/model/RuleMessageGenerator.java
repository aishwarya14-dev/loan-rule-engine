package com.aishwarya.FinBank.ruleengine.model;

import org.springframework.stereotype.Component;

@Component
public class RuleMessageGenerator {

    public String generateMessage(String rule, boolean passed) {
        if (passed) {
            return String.format("Rule '%s' passed ✅", rule);
        } else {
            return String.format("Rule '%s' failed ❌ (Expected %s %s %s)",
                    rule);
        }
    }
}

