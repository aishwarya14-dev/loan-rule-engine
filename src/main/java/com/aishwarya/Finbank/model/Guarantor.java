package com.aishwarya.Finbank.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "guarantor")
@Getter
@Setter
@NoArgsConstructor
public class Guarantor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "loan_application_id", nullable = false)
    private LoanApplication loanApplication;
    @Column(nullable = false)
    private String name;
    @Column(name = "email")
    private String email;
    @Column(name = "phone")
    private String phone;
    @Column(name = "relationship")
    private String relationship;
    @Column(name = "age")
    private Integer age;
    @Column(name = "monthly_income")
    private BigDecimal monthlyIncome;
    @Column(name = "annual_income")
    private BigDecimal annualIncome;
    @Column(name = "credit_score")
    private Integer creditScore;
    @Column(name = "net_worth")
    private BigDecimal netWorth;
    @Column(name = "existing_loans")
    private Integer existingLoans;
    @Column(name = "employment_tenure")
    private Integer employmentTenure;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employment_type_id")
    private EmploymentType employmentType;

    // Timestamps
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public Guarantor(
            LoanApplication loanApplication,
            String name,
            String email,
            String phone,
            String relationship,
            Integer age,
            BigDecimal monthlyIncome,
            BigDecimal annualIncome,
            Integer creditScore,
            BigDecimal netWorth,
            Integer existingLoans,
            Integer employmentTenure,
            EmploymentType employmentType) {

        this.loanApplication = loanApplication;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.relationship = relationship;
        this.age = age;
        this.monthlyIncome = monthlyIncome;
        this.annualIncome = annualIncome;
        this.creditScore = creditScore;
        this.netWorth = netWorth;
        this.existingLoans = existingLoans;
        this.employmentTenure = employmentTenure;
        this.employmentType = employmentType;
    }

    public void updateCreditScore(Integer creditScore) {
        if (creditScore < 300 || creditScore > 900) {
            throw new IllegalArgumentException("Invalid credit score");
        }
        this.creditScore = creditScore;
    }

    public void updateNetWorth(BigDecimal netWorth) {
        if (netWorth == null || netWorth.signum() < 0) {
            throw new IllegalArgumentException("Invalid net worth");
        }
        this.netWorth = netWorth;
    }
}
