package com.aishwarya.FinBank.service;

import com.aishwarya.FinBank.dto.loanApplication.LoanApplicationRequestDto;
import com.aishwarya.FinBank.model.LoanApplication;
import com.aishwarya.FinBank.repository.LoanRepository;
import com.aishwarya.FinBank.ruleengine.service.RuleEngineService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

@Service
public class LoanService {
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    RuleEngineService ruleEngineService;
    @Autowired
    private LoanApplicationMapper mapper;


    public void acceptLoanApplication(LoanApplicationRequestDto application) {
        LoanApplication application1 =  createLoanApplication(application);
        ruleEngineService.evaluateLoanApplication(application1, "STATIC");
        System.out.println("Loan application accepted for: " + application.getApplicantName());
    }

    @Transactional
    public LoanApplication createLoanApplication(LoanApplicationRequestDto dto) {
        // Convert DTO → Entity
        LoanApplication entity = mapper.toEntity(dto);
        return loanRepository.save(entity);
    }
}
