package com.aishwarya.FinBank.dto.loanApplication;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanApplicationResponseDto {
    private String loanType;
    private String applicantName;
    private String applicantEmail;
    private String applicantContact;
    private int creditScore;
    private String remarks;
    private BigDecimal monthlyIncome;
    private int existingLoans;
    private BigDecimal loanAmount;
    private double interestRate;
    private int loanTenureMonths;
}