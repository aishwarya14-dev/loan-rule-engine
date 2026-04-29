package com.aishwarya.FinBank.repository;

import com.aishwarya.FinBank.model.EmploymentType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmploymentTypeRepo extends JpaRepository<EmploymentType, Long> {
    boolean existsByEmploymentType(String value);
}
