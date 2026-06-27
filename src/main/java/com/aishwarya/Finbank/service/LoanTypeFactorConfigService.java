package com.aishwarya.Finbank.service;

import com.aishwarya.Finbank.model.LoanTypeFactorConfig;
import com.aishwarya.Finbank.repository.LoanTypeFactorConfigRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanTypeFactorConfigService {
    @Autowired
    private LoanTypeFactorConfigRepo loanTypeFactorConfigRepo;

    public LoanTypeFactorConfig getLoanTypeFactorConfig(Long loanTypeId, Long factorId){
        return loanTypeFactorConfigRepo
                .findByLoanTypeIdAndFactorId(loanTypeId, factorId)
                .orElseThrow(() -> new RuntimeException("Config not found"));
    }
}
