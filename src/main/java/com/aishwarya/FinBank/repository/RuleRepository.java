package com.aishwarya.FinBank.repository;

import com.aishwarya.FinBank.model.CustomLoanRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleRepository extends JpaRepository<CustomLoanRule,Integer> {}
