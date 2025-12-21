package com.aishwarya.FinBank.dto.loanApplication;

import com.aishwarya.FinBank.model.EmploymentType;
import com.aishwarya.FinBank.model.JobTitle;
import com.aishwarya.FinBank.model.Region;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class LoanApplicationRequestDto {
    @NotNull
    int userId;
    @NotBlank(message = "Loan Type is required")
    String loanType;

    @NotBlank(message = "Name is required")
    String applicantName;
    @NotBlank(message = "Email is required") @Email
    String applicantEmail;
    @NotBlank(message = "Contact is required") @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits")
    String applicantContact;
    @Min(400)
    int creditScore;
    String remarks;
    @DecimalMin("15000")
    BigDecimal monthlyIncome;
    @Max(3)
    int existingLoans;
    @DecimalMin("50000")
    BigDecimal loanAmount;
    @DecimalMin("7.5")
    double interestRate;
    @Positive @Min(15)
    int tenureMonths;
    JobTitle title;
    Region region;
    EmploymentType employmentType;

    public LoanApplicationRequestDto(int userId, String loanType,  String applicantName, String applicantEmail, String applicantContact, int creditScore, String remarks, BigDecimal monthlyIncome, int existingLoans, BigDecimal loanAmount, double interestRate, int tenureMonths) {
        this.userId = userId;
        this.loanType = loanType;
        this.applicantName = applicantName;
        this.applicantEmail = applicantEmail;
        this.applicantContact = applicantContact;
        this.creditScore = creditScore;
        this.remarks = remarks;
        this.monthlyIncome = monthlyIncome;
        this.existingLoans = existingLoans;
        this.loanAmount = loanAmount;
        this.interestRate = interestRate;
        this.tenureMonths = tenureMonths;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public void setApplicantEmail(String applicantEmail) {
        this.applicantEmail = applicantEmail;
    }

    public void setApplicantContact(String applicantContact) {
        this.applicantContact = applicantContact;
    }

    public void setCreditScore(int creditScore) {
        this.creditScore = creditScore;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setExistingLoans(int existingLoans) {
        this.existingLoans = existingLoans;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public void setTenureMonths(int tenureMonths) {
        this.tenureMonths = tenureMonths;
    }

    public void setMonthlyIncome(BigDecimal monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public int getUserId() {
        return userId;
    }

    public String getLoanType() {
        return loanType;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public String getApplicantEmail() {
        return applicantEmail;
    }

    public int getCreditScore() {
        return creditScore;
    }

    public String getApplicantContact() {
        return applicantContact;
    }

    public String getRemarks() {
        return remarks;
    }

    public BigDecimal getMonthlyIncome() {
        return monthlyIncome;
    }

    public int getExistingLoans() {
        return existingLoans;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public int getTenureMonths() {
        return tenureMonths;
    }
}
