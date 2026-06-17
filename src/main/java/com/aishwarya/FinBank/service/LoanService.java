package com.aishwarya.FinBank.service;

import com.aishwarya.FinBank.dto.loanApplication.LoanApplicationRequestDto;
import com.aishwarya.FinBank.mapper.LoanApplicationMapper;
import com.aishwarya.FinBank.model.LoanApplication;
import com.aishwarya.FinBank.repository.LoanRepository;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoanService {

    private LoanRepository loanRepository;

    private RuleEngineService ruleEngineService;

    private LoanApplicationMapper loanApplicationMapper;

    public void acceptLoanApplication(LoanApplicationRequestDto application) {
        // create loan object
        LoanApplication loanApplication =  createLoanApplicationObject(application);

        // send for evaluation
        ruleEngineService.evaluateLoanApplication(loanApplication);
        System.out.println("Loan application accepted for: " + application.getApplicantName());
    }

    public LoanApplication createLoanApplicationObject(LoanApplicationRequestDto dto) {
        // Convert DTO to Entity
        LoanApplication entity = loanApplicationMapper.toEntity(dto);
        return loanRepository.save(entity);
    }
}
