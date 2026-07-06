package com.aishwarya.Finbank.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "co_applicant")
@Getter
@NoArgsConstructor
public class CoApplicant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "loan_application_id", nullable = false)
    private LoanApplication loanApplication;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "contact")
    private String contact;

    @Column(name = "age")
    private Integer age;

    @Column(name = "monthly_income")
    private BigDecimal monthlyIncome;

    @Column(name = "annual_income")
    private BigDecimal annualIncome;

    @Column(name = "credit_score")
    private Integer creditScore;

    @Column(name = "employment_tenure")
    private Integer employmentTenure;

    @Column(name = "company_rating")
    private Integer companyRating;

    @Column(name = "existing_loans")
    private Integer existingLoans;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employment_type_id")
    private EmploymentType employmentType;

    @Builder
    public CoApplicant(
            LoanApplication loanApplication,
            String name,
            String email,
            String contact,
            Integer age,
            BigDecimal monthlyIncome,
            BigDecimal annualIncome,
            Integer creditScore,
            Integer employmentTenure,
            Integer companyRating,
            Integer existingLoans,
            EmploymentType employmentType) {

        this.loanApplication = loanApplication;
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.age = age;
        this.monthlyIncome = monthlyIncome;
        this.annualIncome = annualIncome;
        this.creditScore = creditScore;
        this.employmentTenure = employmentTenure;
        this.companyRating = companyRating;
        this.existingLoans = existingLoans;
        this.employmentType = employmentType;
    }

    public void updateCreditScore(Integer creditScore) {
        if (creditScore < 300 || creditScore > 900) {
            throw new IllegalArgumentException("Invalid credit score");
        }
        this.creditScore = creditScore;
    }

    public void updateCompanyRating(Integer companyRating) {
        if (companyRating != null && (companyRating < 1 || companyRating > 5)) {
            throw new IllegalArgumentException("Invalid company rating");
        }
        this.companyRating = companyRating;
    }
}