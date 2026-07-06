package com.aishwarya.Finbank.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "loan_application")
@NoArgsConstructor
@Getter
public class LoanApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "loan_type_id", nullable = false)
    private LoanType loanType;
    @Column(nullable = false, name = "applicant_name")
    private String applicantName;
    @Column(nullable = false, name = "applicant_email")
    private String applicantEmail;
    @Column(nullable = false, name = "applicant_contact")
    private String applicantContact;
    @Column(nullable = false, name = "credit_score")
    private Integer creditScore;
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;
    @Column(nullable = true, length = 500)
    private String remarks;
    @Column(nullable = false, name = "application_date")
    private LocalDateTime applicationDate;
    @Column(nullable = true, name = "approval_date")
    private LocalDateTime approvalDate;
    @Column(name = "monthly_income", nullable = false)
    private BigDecimal monthlyIncome;
    @Column(name = "existing_loans")
    private Integer existingLoans;
    @Column(name = "loan_amount")
    private BigDecimal loanAmount;
    @Column(name = "interest_rate")
    private Double interestRate;
    @Column(nullable = false, name = "loan_tenure_months")
    private Integer loanTenureMonths;
    @Column(nullable = false)
    private Integer age;
    @Column(name = "company_rating")
    private Integer companyRating;
    @Column(nullable = false, name = "employment_tenure")
    private Integer employmentTenure;
    @ManyToOne
    @JoinColumn(name = "job_title_id")
    private JobTitle jobTitle;
    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;
    @ManyToOne
    @JoinColumn(name = "employment_type_id")
    private EmploymentType employmentType;
    // Additional Attributes
    @ManyToOne
    @JoinColumn(name = "property_type_id")
    private PropertyType propertyType;
    @ManyToOne
    @JoinColumn(name = "industry_id")
    private Industry industry;
    @ManyToOne
    @JoinColumn(name = "loan_purpose_id")
    private LoanPurpose loanPurpose;
    // Credit Profile
    @Column(name = "credit_history_years")
    private Integer creditHistoryYears;
    @Column(name = "total_outstanding_debt")
    private BigDecimal totalOutstandingDebt;
    @Column(name = "credit_card_utilization")
    private Double creditCardUtilization;
    @Column(name = "missed_payments_last_12_months")
    private Integer missedPaymentsLast12Months;
    @Column(name = "bankruptcies")
    private Integer bankruptcies;
    // Income Profile
    @Column(name = "annual_income")
    private BigDecimal annualIncome;
    @Column(name = "other_monthly_income")
    private BigDecimal otherMonthlyIncome;
    @Column(name = "income_verified")
    private Boolean incomeVerified;
    @Column(name = "income_tax_return_available")
    private Boolean incomeTaxReturnAvailable;
    // Employment
    @Column(name = "employer_name")
    private String employerName;
    @Column(name = "probation_completed")
    private Boolean probationCompleted;
    @Column(name = "salary_account_with_bank")
    private Boolean salaryAccountWithBank;
    // Debt Profile
    @Column(name = "monthly_emi")
    private BigDecimal monthlyEmi;
    @Column(name = "debt_to_income_ratio")
    private Double debtToIncomeRatio;
    @Column(name = "loan_defaults")
    private Integer loanDefaults;
    @Column(name = "guarantor_present")
    private Boolean guarantorPresent;
    // Property
    @Column(name = "property_value")
    private BigDecimal propertyValue;
    @Column(name = "property_age")
    private Integer propertyAge;
    @Column(name = "property_verified")
    private Boolean propertyVerified;
    // Loan Details
    @Column(name = "down_payment")
    private BigDecimal downPayment;
    @Column(name = "loan_to_value_ratio")
    private Double loanToValueRatio;
    // Banking Relationship
    @Column(name = "existing_customer")
    private Boolean existingCustomer;
    @Column(name = "customer_since")
    private LocalDate customerSince;
    @Column(name = "average_account_balance")
    private BigDecimal averageAccountBalance;
    @Column(name = "has_fixed_deposit")
    private Boolean hasFixedDeposit;
    // Residence
    @Column(name = "residence_years")
    private Integer residenceYears;
    @Column(name = "owns_house")
    private Boolean ownsHouse;
    // Compliance
    @Column(name = "kyc_verified")
    private Boolean kycVerified;
    @Column(name = "pan_verified")
    private Boolean panVerified;
    @Column(name = "aadhaar_verified")
    private Boolean aadhaarVerified;
    @Column(name = "fraud_flag")
    private Boolean fraudFlag;
    @Column(name = "blacklisted")
    private Boolean blacklisted;
    // Decision
    @OneToOne
    @JoinColumn(name = "result_id")
    private LoanApplicationResult result;

    @Builder
    public LoanApplication(User user, LoanType loanType, String applicantName, String applicantEmail, String applicantContact, Integer creditScore, String remarks, BigDecimal monthlyIncome, Integer existingLoans, BigDecimal loanAmount, Double interestRate, Integer loanTenureMonths, Integer age, Integer companyRating, Integer employmentTenure, JobTitle jobTitle, Region region, EmploymentType employmentType) {

        this.user = user;
        this.loanType = loanType;
        this.applicantName = applicantName;
        this.applicantEmail = applicantEmail;
        this.applicantContact = applicantContact;
        this.creditScore = creditScore;
        this.remarks = remarks;
        this.monthlyIncome = monthlyIncome;
        this.existingLoans = existingLoans != null ? existingLoans : 0;
        this.loanAmount = loanAmount;
        this.interestRate = interestRate;
        this.loanTenureMonths = loanTenureMonths != null ? loanTenureMonths : 15;
        this.age = age;
        this.companyRating = companyRating;
        this.employmentTenure = employmentTenure;
        this.jobTitle = jobTitle;
        this.region = region;
        this.employmentType = employmentType;

        this.status = ApplicationStatus.PENDING;
        this.applicationDate = LocalDateTime.now();

        this.incomeVerified = incomeVerified != null ? incomeVerified : false;
        this.propertyVerified = propertyVerified != null ? propertyVerified : false;
        this.kycVerified = kycVerified != null ? kycVerified : false;
        this.panVerified = panVerified != null ? panVerified : false;
        this.aadhaarVerified = aadhaarVerified != null ? aadhaarVerified : false;
        this.fraudFlag = fraudFlag != null ? fraudFlag : false;
        this.blacklisted = blacklisted != null ? blacklisted : false;
        this.existingCustomer = existingCustomer != null ? existingCustomer : false;
        this.hasFixedDeposit = hasFixedDeposit != null ? hasFixedDeposit : false;
        this.guarantorPresent = guarantorPresent != null ? guarantorPresent : false;
        this.probationCompleted = probationCompleted != null ? probationCompleted : true;
    }

    public void updateCreditScore(Integer creditScore) {
        if (creditScore < 300 || creditScore > 900) {
            throw new IllegalArgumentException("Invalid credit score");
        }
        this.creditScore = creditScore;
    }

    public void updateExistingLoans(Integer existingLoans) {
        if (existingLoans == null || existingLoans < 0) {
            throw new IllegalArgumentException("Invalid existing loans");
        }
        this.existingLoans = existingLoans;
    }

    public void updateInterestRate(Double interestRate) {
        if (interestRate < 7.5 || interestRate > 30) {
            throw new IllegalArgumentException("Invalid interest rate");
        }
        this.interestRate = interestRate;
    }

    public void updateLoanTenureMonths(Integer tenureMonths) {
        if (tenureMonths == null || tenureMonths < 15) {
            throw new IllegalArgumentException("Invalid tenure");
        }
        this.loanTenureMonths = tenureMonths;
    }

    public void updateCompanyRating(Integer companyRating) {
        if (companyRating != null && (companyRating < 1 || companyRating > 5)) {
            throw new IllegalArgumentException("Invalid company rating");
        }
        this.companyRating = companyRating;
    }


    public void approve() {
        if (this.status != ApplicationStatus.PENDING) {
            throw new IllegalStateException("Only pending applications can be approved");
        }
        this.status = ApplicationStatus.APPROVED;
        this.approvalDate = LocalDateTime.now();
    }

    public void reject(String remarks) {
        if (this.status != ApplicationStatus.PENDING) {
            throw new IllegalStateException("Only pending applications can be rejected");
        }
        this.status = ApplicationStatus.REJECTED;
        this.remarks = remarks;
    }

}
