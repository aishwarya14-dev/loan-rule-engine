package com.aishwarya.Finbank.dto.loanApplication;

import com.aishwarya.Finbank.dto.CoApplicantRequest;
import com.aishwarya.Finbank.dto.GuarantorRequest;
import com.aishwarya.Finbank.model.Guarantor;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanApplicationRequestDto {
    // Applicant details
    @NotNull
    private Integer userId;
    @NotBlank(message = "Name is required")
    private String applicantName;
    @NotBlank(message = "Email is required")
    @Email
    private String applicantEmail;
    @NotBlank(message = "Contact is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits")
    private String applicantContact;
    @NotNull
    private Integer regionId;
    @NotNull
    @Min(18)
    private Integer age;

    // Loan Details
    @NotNull
    private Integer loanTypeId;
    @Size(max = 500)
    private String remarks;
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
    private Integer loanPurposeId;
    @NotNull
    private BigDecimal downPayment;

    // Employment Details
    @Positive
    @NotNull
    @Min(0)
    private Integer employmentTenure;
    @NotNull
    private Integer employmentTypeId;
    private Integer industryId;
    private Boolean probationCompleted;
    private Boolean salaryAccountWithBank;
    @NotNull
    private Integer jobTitleId;
    private String employerName;

    // Credit Profile
    @Min(300)
    @Max(900)
    @NotNull
    // supposed to come from bureau in real systems
    private Integer creditScore;
    private Integer creditHistoryYears;
    private BigDecimal totalOutstandingDebt;
    private Double creditCardUtilization;
    private Integer missedPaymentsLast12Months;
    @NotNull
    @PositiveOrZero
    private Integer bankruptcies;

    // Income Profile
    @DecimalMin("15000")
    @NotNull
    private BigDecimal monthlyIncome;
    @NotNull
    @Positive
    private BigDecimal annualIncome;
    private BigDecimal otherMonthlyIncome;
    private Boolean incomeTaxReturnAvailable;

    // Debt Profile
    private BigDecimal monthlyEmi;
    private Integer existingLoans;

    // Property
    private Integer propertyTypeId;
    private BigDecimal propertyValue;
    private Integer propertyAge;

    //banking relationship
    private Boolean hasFixedDeposit;

    //residence
    private Integer residenceYears;
    private Boolean ownsHouse;

    // Compliance to be added at service level

    // Guarantor and Co-Applicant
    private List<GuarantorRequest> guarantors;
    private List<CoApplicantRequest> coApplicants;
}
