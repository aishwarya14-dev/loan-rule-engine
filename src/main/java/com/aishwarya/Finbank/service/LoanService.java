package com.aishwarya.Finbank.service;
import com.aishwarya.Finbank.exceptions.LoanApplicationException;
import com.aishwarya.Finbank.metrics.RuleEngineMetrics;
import com.aishwarya.Finbank.model.CoApplicant;
import com.aishwarya.Finbank.model.Guarantor;
import com.aishwarya.Finbank.model.LoanApplication;

import com.aishwarya.Finbank.dto.loanApplication.LoanApplicationRequestDto;
import com.aishwarya.Finbank.mapper.LoanApplicationMapper;
import com.aishwarya.Finbank.repository.CoApplicantRepo;
import com.aishwarya.Finbank.repository.GuarantorRepo;
import com.aishwarya.Finbank.repository.LoanRepository;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;

    private final RuleEngineService ruleEngineService;

    private final LoanApplicationMapper loanApplicationMapper;

    private final RuleEngineMetrics metrics;

    private final GuarantorRepo guarantorRepo;

    private final CoApplicantRepo coApplicantRepo;

    private final LoanVerificationService verificationService;


    public void acceptLoanApplication(LoanApplicationRequestDto application) {
        // create loan object
        LoanApplication loanApplication = createLoanApplicationObject(application);

        log.info("Evaluating loan application: applicantName={}, loanType={}", loanApplication.getApplicantName(), loanApplication.getLoanType());
        // send for evaluation
        ruleEngineService.evaluateLoanApplication(loanApplication);

        log.info("Accepted loan application: applicantName={}, loanType={}", loanApplication.getApplicantName(), loanApplication.getLoanType());
    }

    private LoanApplication createLoanApplicationObject(LoanApplicationRequestDto dto) {
        log.info("Creating loan application object from DTO: applicantName={}, loanAmount={}", dto.getApplicantName(), dto.getLoanAmount());
        // Convert DTO to Entity
        LoanApplication entity = loanApplicationMapper.toEntity(dto);
        verificationService.implementVerification(entity);
        saveGuarantors(entity.getGuarantors());
        saveCoApplicants(entity.getCoApplicants());
        LoanApplication saved = loanRepository.save(entity);

        if (saved.getId() == null) {
            log.error("Save returned entity with null id for applicant: {}",
                    dto.getApplicantName());
            throw new LoanApplicationException("Failed to save loan application");
        }
        log.info("Loan application saved successfully with id: {}", saved.getId());
        return saved;
    }

    private void saveGuarantors(List<Guarantor> guarantorList){
        for(Guarantor guarantor : guarantorList){
            guarantorRepo.save(guarantor);
        }
    }

    private void saveCoApplicants(List<CoApplicant> coApplicantList){
        for(CoApplicant coApplicant : coApplicantList){
            coApplicantRepo.save(coApplicant);
        }
    }

}
