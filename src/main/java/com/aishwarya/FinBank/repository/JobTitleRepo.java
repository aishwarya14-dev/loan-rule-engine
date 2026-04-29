package com.aishwarya.FinBank.repository;

import com.aishwarya.FinBank.model.JobTitle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobTitleRepo extends JpaRepository<JobTitle, Long> {
    boolean existsByTitle(String value);
}
