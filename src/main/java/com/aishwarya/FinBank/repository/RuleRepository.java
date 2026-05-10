package com.aishwarya.FinBank.repository;

import com.aishwarya.FinBank.model.DslRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleRepository extends JpaRepository<DslRule,Integer> {
    boolean existsByDslRule(String normalized);
}
