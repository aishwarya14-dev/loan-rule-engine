package com.aishwarya.Finbank.exceptions;

public class DuplicateLoanApplicationException extends RuntimeException {
    public DuplicateLoanApplicationException(String s) {
        super(s);
    }
}
