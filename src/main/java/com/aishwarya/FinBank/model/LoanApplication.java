package com.aishwarya.FinBank.model;

import jakarta.persistence.*;
import org.springframework.context.annotation.Scope;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Scope("prototype")
@Entity
@Table(name = "loan_application")
public class LoanApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    int userId;
    LoanType loanType;
    double amount;
    String applicantName;
    String applicantEmail;
    String applicantContact;
    int creditScore;
    ApplicationStatus status;
    String remarks;
    LocalDateTime applicationDate;
    LocalDateTime approvalDate;
    double monthlyIncome;
    int existingLoans;
    BigDecimal loanAmount;
    double interestRate;
    int tenureMonths;
    int age;
    int companyRating;
    int employmentTenure;
    JobTitle title;
    Region region;
    EmploymentType employmentType;

    public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public LoanType getLoanType() {
            return loanType;
        }

        public void setLoanType(LoanType loanType) {
            this.loanType = loanType;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getApplicantName() {
            return applicantName;
        }

        public void setApplicantName(String applicantName) {
            this.applicantName = applicantName;
        }

        public String getApplicantEmail() {
            return applicantEmail;
        }

        public void setApplicantEmail(String applicantEmail) {
            this.applicantEmail = applicantEmail;
        }

        public String getApplicantContact() {
            return applicantContact;
        }

        public void setApplicantContact(String applicantContact) {
            this.applicantContact = applicantContact;
        }

        public int getCreditScore() {
            return creditScore;
        }

        public void setCreditScore(int creditScore) {
            this.creditScore = creditScore;
        }

        public ApplicationStatus getStatus() {
            return status;
        }

        public void setStatus(ApplicationStatus status) {
            this.status = status;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public LocalDateTime getApplicationDate() {
            return applicationDate;
        }

        public void setApplicationDate(LocalDateTime applicationDate) {
            this.applicationDate = applicationDate;
        }

        public LocalDateTime getApprovalDate() {
            return approvalDate;
        }

        public void setApprovalDate(LocalDateTime approvalDate) {
            this.approvalDate = approvalDate;
        }

        public double getMonthlyIncome() {
            return monthlyIncome;
        }

        public void setMonthlyIncome(double monthlyIncome) {
            this.monthlyIncome = monthlyIncome;
        }

        public int getExistingLoans() {
            return existingLoans;
        }

        public void setExistingLoans(int existingLoans) {
            this.existingLoans = existingLoans;
        }

        public BigDecimal getLoanAmount() {
            return loanAmount;
        }

        public void setLoanAmount(BigDecimal loanAmount) {
            this.loanAmount = loanAmount;
        }

        public double getInterestRate() {
            return interestRate;
        }

        public void setInterestRate(double interestRate) {
            this.interestRate = interestRate;
        }

        public int getTenureMonths() {
            return tenureMonths;
        }

        public void setTenureMonths(int tenureMonths) {
            this.tenureMonths = tenureMonths;
        }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getCompanyRating() {
        return companyRating;
    }

    public void setCompanyRating(int companyRating) {
        this.companyRating = companyRating;
    }

    public int getEmploymentTenure() {
        return employmentTenure;
    }

    public void setEmploymentTenure(int employmentTenure) {
        this.employmentTenure = employmentTenure;
    }

    public JobTitle getJobTitle() {
        return title;
    }

    public void setJobTitle(JobTitle title) {
        this.title = title;
    }

    public Region getApplicantRegion() {
        return region;
    }

    public void setApplicantRegion(Region region) {
        this.region = region;
    }

    public EmploymentType getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(EmploymentType type) {
        this.employmentType = type;
    }

}
