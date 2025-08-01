package com.aishwarya.FinBank.service;

import com.aishwarya.FinBank.model.LoanApplication;
import com.aishwarya.FinBank.repository.LoanRepository;
import com.aishwarya.FinBank.ruleengine.service.RuleEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class LoanService {
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    RuleEngineService ruleEngineService;


    public void acceptLoanApplication(LoanApplication application) {
        loanRepository.save(application);
        System.out.println("Loan application accepted for: " + application.getApplicantName());
    }
}
