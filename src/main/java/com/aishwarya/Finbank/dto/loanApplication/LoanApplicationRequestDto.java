package com.aishwarya.Finbank.dto.loanApplication;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanApplicationRequestDto {
    @NotNull
    private Integer userId;
    @NotNull
    private Integer loanTypeId;
    @NotBlank(message = "Name is required")
    private String applicantName;
    @NotBlank(message = "Email is required")
    @Email
    private String applicantEmail;
    @NotBlank(message = "Contact is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits")
    private String applicantContact;
    @Min(300)
    @Max(900)
    @NotNull
    private Integer creditScore;
    @Size(max = 500)
    private String remarks;
    @DecimalMin("15000")
    @NotNull
    private BigDecimal monthlyIncome;
    @Max(3)
    private Integer existingLoans;
    @DecimalMin("50000")
    @NotNull
    @Positive
    private BigDecimal loanAmount;
    @DecimalMin("7.5")
    @DecimalMax("30.0")
    @NotNull
    private Double interestRate;
    @Positive
    @Min(15)
    @NotNull
    private Integer loanTenureMonths;
    @NotNull
    private Integer jobTitleId;
    @NotNull
    private Integer regionId;
    @NotNull
    private Integer employmentTypeId;
    @NotNull
    @Min(18)
    private Integer age;
    private Integer companyRating;
    @Positive
    @NotNull
    @Min(0)
    private Integer employmentTenure;
    private Integer propertyTypeId;
    private Integer loanPurposeId;
    private Integer industryId;
    private Integer creditHistoryYears;
    private BigDecimal totalOutstandingDebt;
    private Double creditCardUtilization;
    private Integer missedPaymentsLast12Months;
    @NotNull
    @NotBlank(message = "Please specify if the applicant has any bankruptcies")
    private Integer bankruptcies;
    @NotNull
    @NotBlank
    private BigDecimal annualIncome;
    private BigDecimal otherMonthlyIncome;
    @NotBlank
    private Boolean incomeVerified;
    private Boolean incomeTaxReturnAvailable;
    private String employerName;
    private Boolean probationCompleted;
    private Boolean salaryAccountWithBank;

}
