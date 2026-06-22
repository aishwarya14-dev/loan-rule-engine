package com.aishwarya.Finbank.dto.rules.dynamicrules;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RulesRequestDto {
    @NotBlank(message = "Rule text cannot be blank")
    private String dslRule;
    @NotNull
    private Long loanTypeId;

    public String getRuleText() {
        return dslRule;
    }
}
