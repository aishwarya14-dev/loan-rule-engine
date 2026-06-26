package com.aishwarya.Finbank.dto.rules.dynamicrules;

import com.aishwarya.Finbank.enums.RuleSeverity;
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
    @NotNull
    private Integer factorId;
    @NotNull
    private Double evidenceWeight;
    private RuleSeverity ruleSeverity;

    public String getRuleText() {
        return dslRule;
    }
}
