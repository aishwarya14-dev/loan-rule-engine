package com.aishwarya.FinBank.exceptions;

import java.util.ArrayList;
import java.util.List;

public class DslValidationException extends RuntimeException{
    private List<String> errors = new ArrayList<>();
    public DslValidationException(List<String> errors ) {
        super("DSL validation failed");
        this.errors = errors;
    }

    public List<String> getErrors() { return errors; }
}
