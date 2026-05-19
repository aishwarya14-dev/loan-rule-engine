package com.aishwarya.FinBank.repository;

import com.aishwarya.FinBank.model.DslRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RuleRepository extends JpaRepository<DslRule,Long> {
    boolean existsByDslRule(String normalized);
    List<DslRule> findByLoanTypeLoanType(String loanType);
}
