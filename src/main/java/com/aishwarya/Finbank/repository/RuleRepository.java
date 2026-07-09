package com.aishwarya.Finbank.repository;

import com.aishwarya.Finbank.model.DslRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RuleRepository extends JpaRepository<DslRule, Long> {
    boolean existsByDslRule(String normalized);

    List<DslRule> findByLoanTypeLoanType(String loanType);

    List<DslRule> findByDslRule(String normalized);

    boolean existsByDslRuleAndLoanTypeId(String normalized , Long loanTypeId);
}
