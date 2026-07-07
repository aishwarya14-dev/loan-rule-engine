package com.aishwarya.Finbank.dto;

import com.aishwarya.Finbank.model.EmploymentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuarantorRequest {
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @NotBlank
    private Integer age;
    private String email;
    @NotNull
    @NotBlank
    private String phone;
    @NotNull
    @NotBlank
    private String relationship;
    @NotNull
    @NotBlank
    private BigDecimal monthlyIncome;
    @NotNull
    @NotBlank
    private BigDecimal annualIncome;
    @NotNull
    @NotBlank
    private Integer creditScore;
    @NotNull
    @NotBlank
    private BigDecimal netWorth;
    @NotNull
    @NotBlank
    private Integer existingLoans;
    private Integer employmentTenure;
    @NotNull
    @NotBlank
    private EmploymentType employmentType;
}