package com.aishwarya.FinBank.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "loan_application")
@NoArgsConstructor
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
    @Column(nullable = false,name = "applicant_name")
    private String applicantName;
    @Column(nullable = false,name = "applicant_email")
    private String applicantEmail;
    @Column(nullable = false,name = "applicant_contact")
    private String applicantContact;
    @Column(nullable = false, name = "credit_score")
    private Integer creditScore;
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;
    @Column(nullable = true, length = 500)
    private String remarks;
    @Column(nullable = false, name = "application_date")
    private LocalDateTime applicationDate;
    @Column(nullable = true,name = "approval_date")
    private LocalDateTime approvalDate;
    @Column(name = "monthly_income",nullable = false)
    private BigDecimal monthlyIncome;
    @Column(name = "existing_loans")
    private Integer existingLoans;
    @Column(name = "loan_amount")
    private BigDecimal loanAmount;
    @Column(name = "interest_rate")
    private Double interestRate;
    @Column(nullable = false , name = "loan_tenure_months")
    private Integer loanTenureMonths;
    @Column(nullable = false)
    private Integer age;
    @Column(name = "company_rating")
    private Integer companyRating;
    @Column(nullable = false , name = "employment_tenure")
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
    }


        public Integer getId() {
            return id;
        }

        public User getUser() {
            return user;
        }
        public LoanType getLoanType() {
            return loanType;
        }

        public String getApplicantName() {
            return applicantName;
        }

        public String getApplicantEmail() {
            return applicantEmail;
        }

        public String getApplicantContact() {
            return applicantContact;
        }

        public Integer getCreditScore() {
            return creditScore;
        }

        public void updateCreditScore(Integer creditScore) {
            if (creditScore < 300 || creditScore > 900) {
                throw new IllegalArgumentException("Invalid credit score");
            }
            this.creditScore = creditScore;
        }

        public ApplicationStatus getStatus() {
            return status;
        }

        public String getRemarks() {
            return remarks;
        }

        public void updateRemarks(String remarks) {
            this.remarks = remarks;
        }

        public LocalDateTime getApplicationDate() {
            return applicationDate;
        }

        public LocalDateTime getApprovalDate() {
            return approvalDate;
        }

        public BigDecimal getMonthlyIncome() {
            return monthlyIncome;
        }

        public Integer getExistingLoans() {
            return existingLoans;
        }

        public void updateExistingLoans(Integer existingLoans) {
            if (existingLoans == null || existingLoans < 0) {
                throw new IllegalArgumentException("Invalid existing loans");
            }
            this.existingLoans = existingLoans;
        }

        public BigDecimal getLoanAmount() {
            return loanAmount;
        }

        public Double getInterestRate() {
            return interestRate;
        }

        public void updateInterestRate(Double interestRate) {
           if (interestRate < 7.5 || interestRate > 30) {
            throw new IllegalArgumentException("Invalid interest rate");
          }
        this.interestRate = interestRate;
        }

        public Integer getLoanTenureMonths() {
            return loanTenureMonths;
        }

        public void updateLoanTenureMonths(Integer tenureMonths) {
            if (tenureMonths == null || tenureMonths < 15) {
                throw new IllegalArgumentException("Invalid tenure");
            }
            this.loanTenureMonths = tenureMonths;
        }

        public Integer getAge() {
        return age;
    }

        public Integer getCompanyRating() {
        return companyRating;
    }

        public void updateCompanyRating(Integer companyRating) {
            if (companyRating != null && (companyRating < 1 || companyRating > 5)) {
                throw new IllegalArgumentException("Invalid company rating");
            }
            this.companyRating = companyRating;
    }

        public Integer getEmploymentTenure() {
          return employmentTenure;
        }
        public JobTitle getJobTitle() {
        return jobTitle;
    }

        public Region getRegion() {
        return region;
    }

        public EmploymentType getEmploymentType() {
        return employmentType;
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
