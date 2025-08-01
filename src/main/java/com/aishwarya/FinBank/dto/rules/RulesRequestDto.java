package com.aishwarya.FinBank.dto.rules;

import jakarta.validation.constraints.NotBlank;

public class RulesRequestDto {
    @NotBlank(message = "Rule text cannot be blank")
    private String dslRule;

    public String getRuleText() {
        return dslRule;
    }
}
