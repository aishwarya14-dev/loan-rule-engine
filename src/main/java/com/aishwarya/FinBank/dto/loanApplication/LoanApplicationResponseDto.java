package com.aishwarya.FinBank.dto.loanApplication;

import java.math.BigDecimal;

public class LoanApplicationResponseDto {
    String loanType;
    String applicantName;
    String applicantEmail;
    String applicantContact;
    int creditScore;
    String remarks;
    BigDecimal monthlyIncome;
    int existingLoans;
    BigDecimal loanAmount;
    double interestRate;
    int tenureMonths;

    public LoanApplicationResponseDto( String loanType,  String applicantName, String applicantEmail, String applicantContact, int creditScore, String remarks, BigDecimal monthlyIncome, int existingLoans, BigDecimal loanAmount, double interestRate, int tenureMonths) {
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
