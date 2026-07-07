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
@Table(name = "co_applicant")
@Getter
@Setter
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
    @Column(name = "phone")
    private String phone;
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

    // Timestamps
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public CoApplicant(
            LoanApplication loanApplication,
            String name,
            String email,
            String phone,
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
        this.phone = phone;
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