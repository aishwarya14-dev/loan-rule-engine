package com.aishwarya.FinBank.repository;

import com.aishwarya.FinBank.model.LoanType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanTypeRepo extends JpaRepository<LoanType, Long> {
}
