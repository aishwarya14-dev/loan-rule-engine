package com.aishwarya.FinBank.builder;
import com.aishwarya.FinBank.model.LoanApplication;


public class LoanApplicationBuilder {
    private static LoanApplication createLoanObject() {
        return LoanApplication.builder()

                .build();
    }
}
