package com.aishwarya.Finbank.repository;

import com.aishwarya.Finbank.model.LoanTypeFactorConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoanTypeFactorConfigRepo extends JpaRepository<LoanTypeFactorConfig,Long> {
    Optional<LoanTypeFactorConfig> findByLoanTypeIdAndFactorId(
            Long loanTypeId,
            Long factorId
    );
}
