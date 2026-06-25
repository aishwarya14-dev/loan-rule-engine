package com.aishwarya.Finbank.repository;

import com.aishwarya.Finbank.model.LoanApplicationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanApplicationResultRepo extends JpaRepository<LoanApplicationResult,Long> {
}
