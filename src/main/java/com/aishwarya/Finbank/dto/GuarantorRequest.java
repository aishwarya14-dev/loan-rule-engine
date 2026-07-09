package com.aishwarya.Finbank.dto;

import com.aishwarya.Finbank.model.EmploymentType;
import jakarta.persistence.Column;
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
    @NotBlank
    private String name;
    @NotNull
    private Integer age;
    private String email;
    @NotBlank
    private String phone;
    @NotBlank
    private String relationship;
    @NotNull
    private BigDecimal monthlyIncome;
    private BigDecimal annualIncome;
    @NotNull
    private Integer creditScore;
    private BigDecimal netWorth;
    @NotNull
    private Integer existingLoans;
    private Integer employmentTenure;
    @NotNull
    private Integer employmentTypeId;
}