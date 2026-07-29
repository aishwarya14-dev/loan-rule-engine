package com.aishwarya.Finbank.mapper;

import com.aishwarya.Finbank.dto.loanApplication.LoanApplicationResponseDto;
import com.aishwarya.Finbank.model.LoanApplicationResult;
import org.springframework.stereotype.Component;

@Component
public class LoanApplicationResultMapper {
    public LoanApplicationResponseDto toResponse(LoanApplicationResult result) {
        return new LoanApplicationResponseDto(
                result.getApplication().getId(),
                result.getApplication().getApplicantName(),
                result.getApplication().getApplicantEmail(),
                result.getApplication().getApplicantContact(),
                result.getApplication().getAge(),
                result.getApplication().getRegion().getRegionName(),
                result.getApplication().getLoanTenureMonths(),
                result.getApplication().getDownPayment(),
                result.getApplication().getApprovalDate(),
                result.getApplication().getInterestRate(),
                result.getApplication().getLoanType().getLoanType(),
                result.getApplication().getLoanAmount(),
                result.getFinalScore(),
                result.getDecision().name()
        );
    }
}
