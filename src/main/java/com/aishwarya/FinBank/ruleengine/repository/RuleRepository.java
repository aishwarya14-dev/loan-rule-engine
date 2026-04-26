package com.aishwarya.FinBank.ruleengine.repository;

import com.aishwarya.FinBank.ruleengine.model.DslRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleRepository extends JpaRepository<DslRule,Integer> {}
