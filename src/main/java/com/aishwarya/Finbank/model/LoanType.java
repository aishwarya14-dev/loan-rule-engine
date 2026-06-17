package com.aishwarya.Finbank.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity(name = "loan_type")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
public class LoanType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "loan_type")
    private String loanType;
    private String description;
    @Column(name = "interest_rate")
    private Double interestRate;
    @Column(name = "max_term_in_months")
    private int maxTermInMonths;
    @Column(name = "max_loan_amount")
    private BigDecimal maxLoanAmount;
}
