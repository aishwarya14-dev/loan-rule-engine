package com.aishwarya.Finbank.service;
import com.aishwarya.Finbank.model.LoanApplication;

import com.aishwarya.Finbank.dto.loanApplication.LoanApplicationRequestDto;
import com.aishwarya.Finbank.mapper.LoanApplicationMapper;
import com.aishwarya.Finbank.repository.LoanRepository;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class LoanService {

    private LoanRepository loanRepository;

    private RuleEngineService ruleEngineService;

    private LoanApplicationMapper loanApplicationMapper;

    public void acceptLoanApplication(LoanApplicationRequestDto application) {
        // create loan object
        LoanApplication loanApplication = createLoanApplicationObject(application);

        log.info("Evaluating loan application: applicantName={}, loanType={}", loanApplication.getApplicantName(), loanApplication.getLoanType());
        // send for evaluation
        ruleEngineService.evaluateLoanApplication(loanApplication);

        log.info("Accepted loan application: applicantName={}, loanType={}", loanApplication.getApplicantName(), loanApplication.getLoanType());
    }

    public LoanApplication createLoanApplicationObject(LoanApplicationRequestDto dto) {
        log.info("Creating loan application object from DTO: applicantName={}, loanAmount={}", dto.getApplicantName(), dto.getLoanAmount());
        // Convert DTO to Entity
        LoanApplication entity = loanApplicationMapper.toEntity(dto);
        return loanRepository.save(entity);
    }
}
