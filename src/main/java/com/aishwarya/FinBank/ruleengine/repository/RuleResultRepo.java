package com.aishwarya.FinBank.ruleengine.repository;

import com.aishwarya.FinBank.ruleengine.model.RuleResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RuleResultRepo extends JpaRepository<RuleResult, Long> {
}
