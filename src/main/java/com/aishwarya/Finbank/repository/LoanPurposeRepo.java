package com.aishwarya.Finbank.repository;

import com.aishwarya.Finbank.model.LoanPurpose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanPurposeRepo extends JpaRepository<LoanPurpose, Long> {
}
