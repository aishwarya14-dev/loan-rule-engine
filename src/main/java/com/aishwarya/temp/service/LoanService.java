package com.aishwarya.Finbank.service;
import com.aishwarya.Finbank.model.LoanApplication;

import com.aishwarya.Finbank.dto.loanApplication.LoanApplicationRequestDto;
import com.aishwarya.Finbank.mapper.LoanApplicationMapper;
import com.aishwarya.Finbank.repository.LoanRepository;
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
        LoanApplication loanApplication = createLoanApplicationObject(application);

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
