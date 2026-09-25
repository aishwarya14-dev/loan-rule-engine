package com.aishwarya.Finbank.exceptions;

public class LoanApplicationException extends RuntimeException {
    public LoanApplicationException(String failedToSaveLoanApplication) {
        super(failedToSaveLoanApplication);
    }
}
