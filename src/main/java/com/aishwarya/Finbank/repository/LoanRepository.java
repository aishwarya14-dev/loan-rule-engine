package com.aishwarya.Finbank.repository;

import com.aishwarya.Finbank.enums.ApplicationStatus;
import com.aishwarya.Finbank.model.LoanApplication;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<LoanApplication, Integer> {
    List<LoanApplication> findByApplicantName(String name);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<LoanApplication> findByUserIdAndLoanTypeIdAndStatusWithLock(
            Long userId, Long loanTypeId, ApplicationStatus status
    );
}
