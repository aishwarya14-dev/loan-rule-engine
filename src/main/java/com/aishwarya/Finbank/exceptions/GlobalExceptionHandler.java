package com.aishwarya.Finbank.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

import static org.springframework.http.ResponseEntity.status;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleBadRequest(IllegalArgumentException ex) {
        log.error("Invalid input", ex);
        return status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleBadRequest(IllegalStateException ex) {
        log.error("Invalid input", ex);
        return status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleBadRequest(UserNotFoundException ex) {
        log.error("Invalid input", ex);
        return status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(
            MethodArgumentNotValidException ex) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .toList();

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(DslValidationException.class)
    public ResponseEntity<?> handleDslValidationException(DslValidationException ex) {
        return ResponseEntity.badRequest().body(Map.of(
                "status", "INVALID_RULE",
                "errors", ex.getErrors()
        ));
    }

    @ExceptionHandler(DslParsingException.class)
    public ResponseEntity<?> handleDslParsingException(DslParsingException ex) {
        return ResponseEntity.badRequest().body("INVALID_RULE");
    }

    @ExceptionHandler(LoanEvaluationException.class)
    public ResponseEntity<?> handleLoanEvaluationException(LoanEvaluationException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Service Unavailable");
    }

    @ExceptionHandler(UserCreationException.class)
    public ResponseEntity<?> handleUserCreationException(UserCreationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create user");
    }

    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<?> handleDuplicateUserException(DuplicateUserException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username OR Mobile number already exists");
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentialsException(
            BadCredentialsException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Invalid username or password");
    }

    @ExceptionHandler(InvalidRuleConfigurationException.class)
    public ResponseEntity<String> handleInvalidRuleConfigurationException(
            InvalidRuleConfigurationException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Invalid rule configuration" + " " + ex.getMessage());
    }

    @ExceptionHandler(RuleEvaluationException.class)
    public ResponseEntity<String> handleRuleEvaluationException(
            RuleEvaluationException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Error during rule evaluation " + ex.getMessage());
    }

    @ExceptionHandler(LoanApplicationException.class)
    public ResponseEntity<String> handleLoanApplicationException(
            LoanApplicationException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Loan application Error " + ex.getMessage());
    }
}
