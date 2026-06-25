package com.aishwarya.Finbank.repository;

import com.aishwarya.Finbank.model.ApplicationStatus;
import com.aishwarya.Finbank.model.LoanApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<LoanApplication, Integer> {
    List<LoanApplication> findByApplicantName(String name);

    Long findByUserIdAndLoanTypeIdAndStatus(
            Long userId, Long loanTypeId, ApplicationStatus status
    );
}
