package com.aishwarya.FinBank.builder;
import com.aishwarya.FinBank.model.*;

import java.math.BigDecimal;


public class LoanApplicationBuilder {
    private static LoanApplication createLoanObject() {
        User user = createUser();
        LoanType loanType = createLoanType();
        JobTitle jobTitle = createJobTitle();
        EmploymentType employmentType = createEmploymentType();
        Region region = createRegion();
        return LoanApplication.builder()
                .user(user)
                .age(25)
                .existingLoans(0)
                .loanAmount(BigDecimal.valueOf(100000))
                .applicantContact("7689067876")
                .applicantEmail("xyd@gmail.com")
                .applicantName("Jatin Tanwar")
                .loanType(loanType)
                .loanTenureMonths(20)
                .companyRating(6)
                .creditScore(680)
                .interestRate(9.0)
                .jobTitle(jobTitle)
                .region(region)
                .employmentType(employmentType)
                .build();
    }

    public static User createUser(){
        return User.builder()
                .role("USER")
                .mobileNumber("7898675444")
                .username("Jatin")
                .build();
    }

    public static LoanType createLoanType(){
        return new LoanType().builder()
                .loanType("PERSONAL")
                        .maxLoanAmount(BigDecimal.valueOf(100000))
                                .interestRate(12.0)
                                        .maxTermInMonths(60).build();
    }

    public static JobTitle createJobTitle(){
        return new JobTitle().builder().jobTitle("TEACHER").build();
    }

    public static Region createRegion(){
        return new Region().builder().regionName("NORTH").build();
    }

    public static EmploymentType createEmploymentType(){
        return new EmploymentType().builder().employmentType("SALARIED").build();
    }
}
