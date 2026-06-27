package com.aishwarya.Finbank.repository;

import com.aishwarya.Finbank.model.LoanType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanTypeRepo extends JpaRepository<LoanType, Long> {
    boolean existsByLoanType(String value);
}
