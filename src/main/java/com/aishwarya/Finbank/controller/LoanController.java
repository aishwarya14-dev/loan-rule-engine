package com.aishwarya.Finbank.controller;
import com.aishwarya.Finbank.dto.loanApplication.LoanApplicationResponseDto;
import com.aishwarya.Finbank.model.DslRule;
import com.aishwarya.Finbank.model.LoanApplication;

import com.aishwarya.Finbank.dto.loanApplication.LoanApplicationRequestDto;
import com.aishwarya.Finbank.model.LoanApplicationResult;
import com.aishwarya.Finbank.service.LoanService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/loan")
@Tag(name = "Loan APIs" , description = "Submit loan application")
public class LoanController {

    @Autowired
    private LoanService service;

    @PostMapping("/loanApplication")
    public ResponseEntity<LoanApplicationResult> acceptLoanApplication(@RequestBody LoanApplicationRequestDto application) {
        log.info("POST /loanApplication - username={}, amount={}", application.getApplicantName(), application.getLoanAmount());
        LoanApplicationResult loanApplicationResult = service.acceptLoanApplication(application);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(loanApplicationResult);
    }
}
