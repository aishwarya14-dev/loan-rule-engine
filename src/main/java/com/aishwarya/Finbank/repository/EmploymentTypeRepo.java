package com.aishwarya.Finbank.repository;


import com.aishwarya.Finbank.model.EmploymentType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmploymentTypeRepo extends JpaRepository<EmploymentType, Long> {
    boolean existsByEmploymentType(String value);
}
