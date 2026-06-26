package com.aishwarya.Finbank.repository;

import com.aishwarya.Finbank.model.FactorEvaluationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FactorEvaluationResultRepo extends JpaRepository<FactorEvaluationResult,Long> {
}
