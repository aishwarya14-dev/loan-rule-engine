package com.aishwarya.Finbank.controller;
import com.aishwarya.Finbank.model.LoanApplication;

import com.aishwarya.Finbank.dto.loanApplication.LoanApplicationRequestDto;
import com.aishwarya.Finbank.service.LoanService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void acceptLoanApplication(@RequestBody LoanApplicationRequestDto application) {
        log.info("POST /loanApplication - username={}, amount={}", application.getApplicantName(), application.getLoanAmount());
        service.acceptLoanApplication(application);
    }
}
