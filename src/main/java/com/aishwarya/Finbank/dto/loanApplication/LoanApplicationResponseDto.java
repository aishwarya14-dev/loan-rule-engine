package com.aishwarya.Finbank.dto.loanApplication;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoanApplicationResponseDto {
    private Integer applicationId;
    private String applicantName;
    private String applicantEmail;
    private String applicantContact;
    private Integer age;
    private String region;

    private Integer loanTenureMonths;
    private BigDecimal downPayment;
    private LocalDateTime approvalDate;
    private Double interestRate;
    private String loanType;
    private BigDecimal loanAmount;

    private Double finalScore;
    private String decision;
}