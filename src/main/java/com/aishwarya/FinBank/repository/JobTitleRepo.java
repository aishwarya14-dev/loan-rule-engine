package com.aishwarya.Finbank.repository;


import com.aishwarya.Finbank.model.JobTitle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobTitleRepo extends JpaRepository<JobTitle, Long> {
    boolean existsByJobTitle(String value);
}
