package com.aishwarya.Finbank.repository;

import com.aishwarya.Finbank.model.Industry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndustryRepo extends JpaRepository<Industry, Long> {
    boolean existsByName(String value);
}
