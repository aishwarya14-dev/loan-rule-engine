package com.aishwarya.FinBank.dto.rules;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RulesRequestDto {
    @NotBlank(message = "Rule text cannot be blank")
    private String dslRule;
    @NotNull
    private Integer loanTypeId;

    public String getRuleText() {
        return dslRule;
    }
}
