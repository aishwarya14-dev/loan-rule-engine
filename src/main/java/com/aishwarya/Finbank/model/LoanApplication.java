package com.aishwarya.Finbank.model;

import com.aishwarya.Finbank.enums.ApplicationStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "loan_application")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class LoanApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Applicant Details
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(nullable = false, name = "applicant_name")
    private String applicantName;
    @Column(nullable = false, name = "applicant_email")
    private String applicantEmail;
    @Column(nullable = false, name = "applicant_contact")
    private String applicantContact;
    @Column(nullable = false)
    private Integer age;
    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

    // Loan Details
    @ManyToOne
    @JoinColumn(name = "loan_type_id", nullable = false)
    private LoanType loanType;
    @Column(nullable = true, length = 500)
    private String remarks;
    @CreationTimestamp
    @Column(nullable = false, name = "application_date")
    private LocalDateTime applicationDate;
    @Column(nullable = true, name = "approval_date")
    private LocalDateTime approvalDate;
    @Column(name = "loan_amount")
    private BigDecimal loanAmount;
    @Column(name = "interest_rate")
    private Double interestRate;
    @Column(nullable = false, name = "loan_tenure_months")
    private Integer loanTenureMonths;
    @ManyToOne
    @JoinColumn(name = "loan_purpose_id")
    private LoanPurpose loanPurpose;
    @Column(name = "down_payment")
    private BigDecimal downPayment;

    // Credit Profile
    @Column(nullable = false, name = "credit_score")
    private Integer creditScore;
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
    @Column(name = "monthly_income", nullable = false)
    private BigDecimal monthlyIncome;
    @Column(name = "annual_income")
    private BigDecimal annualIncome;
    @Column(name = "other_monthly_income")
    private BigDecimal otherMonthlyIncome;
    @Column(name = "income_verified")
    private Boolean incomeVerified;
    @Column(name = "income_tax_return_available")
    private Boolean incomeTaxReturnAvailable;

    // Employment
    @ManyToOne
    @JoinColumn(name = "employment_type_id")
    private EmploymentType employmentType;
    @Column(name = "employer_name")
    private String employerName;
    @Column(name = "company_rating")
    private Integer companyRating;
    @Column(nullable = false, name = "employment_tenure")
    private Integer employmentTenure;
    @ManyToOne
    @JoinColumn(name = "job_title_id")
    private JobTitle jobTitle;
    @Column(name = "probation_completed")
    private Boolean probationCompleted;
    @Column(name = "salary_account_with_bank")
    private Boolean salaryAccountWithBank;
    @ManyToOne
    @JoinColumn(name = "industry_id")
    private Industry industry;

    // Debt Profile
    @Column(name = "existing_loans")
    private Integer existingLoans;
    @Column(name = "monthly_emi")
    private BigDecimal monthlyEmi;
    @Column(name = "debt_to_income_ratio")
    private Double debtToIncomeRatio;
    @Column(name = "loan_defaults")
    private Integer loanDefaults;

    // Property
    @ManyToOne
    @JoinColumn(name = "property_type_id")
    private PropertyType propertyType;
    @Column(name = "property_value")
    private BigDecimal propertyValue;
    @Column(name = "property_age")
    private Integer propertyAge;
    @Column(name = "property_verified")
    private Boolean propertyVerified;

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
    @Column(name = "loan_to_value_ratio")
    private Double loanToValueRatio;
    @Column(name = "guarantor_present")
    private Boolean guarantorPresent;
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

    // Guarantor and Co-Applicant
    @OneToMany(mappedBy = "loanApplication",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<Guarantor> guarantors = new ArrayList<>();
    @OneToMany(mappedBy="loanApplication",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<CoApplicant> coApplicants = new ArrayList<>();


    // Timestamps
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Decision
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;
    @OneToOne
    @JoinColumn(name = "result_id")
    private LoanApplicationResult result;

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

    public void updateApprovalDate(LocalDateTime approvalDate){
        if (approvalDate == null) {
            throw new IllegalArgumentException("Approval date cannot be null");
        }
        if (approvalDate.isBefore(applicationDate)) {
            throw new IllegalArgumentException(
                    "Approval date cannot be before application date");
        }
        this.approvalDate = approvalDate;
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

    public void addGuarantor(Guarantor guarantor) {
        guarantors.add(guarantor);
        guarantor.setLoanApplication(this);
    }

    public void removeCoApplicant(Guarantor guarantor) {
        guarantors.remove(guarantor);
        guarantor.setLoanApplication(null);
    }

    public void addCoApplicant(CoApplicant coApplicant) {
        coApplicants.add(coApplicant);
        coApplicant.setLoanApplication(this);
    }

    public void removeCoApplicant(CoApplicant coApplicant) {
        coApplicants.remove(coApplicant);
        coApplicant.setLoanApplication(null);
    }

    public void updateLoanToValueRatio(Double loanToValueRatio){
        if(loanToValueRatio == null){
            throw new IllegalArgumentException("loanToValue ratio cannot be null");
        }
        if (loanToValueRatio < 0 || loanToValueRatio > 100) {
            throw new IllegalArgumentException("Loan-to-value ratio must be between 0 and 100");
        }
        this.loanToValueRatio = loanToValueRatio;
    }

    public void updateDebtToIncomeRatio(Double debtToIncomeRatio){
        if(debtToIncomeRatio == null){
            throw new IllegalArgumentException("Debt-To-Income ratio cannot be null");
        }
        if (debtToIncomeRatio < 0 || debtToIncomeRatio > 100) {
            throw new IllegalArgumentException("Debt-To-Income ratio must be between 0 and 100");
        }
        this.debtToIncomeRatio = debtToIncomeRatio;
    }

    public void updateIncomeVerified(Boolean incomeVerified){
        if(incomeVerified == null){
            throw new IllegalArgumentException("Income verification status must be provided.");
        }
        this.incomeVerified = incomeVerified;
    }

    public void UpdateGuarantorsVerified(Boolean guarantorPresent){
        if(guarantorPresent == null){
            throw new IllegalArgumentException("Guarantor status must be provided.");
        }
        this.guarantorPresent = guarantorPresent;
    }

    public void updateKycVerified(Boolean kycVerified){
        this.kycVerified = kycVerified;
    }

    public void updateAadharVerified(Boolean aadhaarVerified){
        this.aadhaarVerified = aadhaarVerified;
    }

    public void updatePanVerifiied(Boolean panVerified){
        this.panVerified = panVerified;
    }

    public void updatePropertyVerified(Boolean propertyVerified){
        this.propertyVerified = propertyVerified;
    }

    public void updateFraudFlag(Boolean fraudFlag){
        this.fraudFlag = fraudFlag;
    }

    public void updateBlacklistingStatus(Boolean blacklisted){
        this.blacklisted = blacklisted;
    }

    public void updateExistingCustomer(Boolean existingCustomer){
        this.existingCustomer = existingCustomer;
    }

    public void updateLoanDefaults(Integer loanDefaults){
        this.loanDefaults = loanDefaults;
    }

    public void updateAverageAccountBalance(BigDecimal averageAccountBalance){
        this.averageAccountBalance = averageAccountBalance;
    }

    public void updateHasFixedDeposit(Boolean hasFixedDeposit){
        this.hasFixedDeposit = hasFixedDeposit;
    }
}
