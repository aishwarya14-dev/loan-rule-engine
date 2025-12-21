package com.aishwarya.FinBank.controller;

import com.aishwarya.FinBank.dto.loanApplication.LoanApplicationRequestDto;
import com.aishwarya.FinBank.model.LoanApplication;
import com.aishwarya.FinBank.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoanController {

    @Autowired
    private LoanService service;

    @PostMapping("/loanApplication")
    public void acceptLoanApplication(@RequestBody LoanApplicationRequestDto application){
       service.acceptLoanApplication(application);
    }
}
