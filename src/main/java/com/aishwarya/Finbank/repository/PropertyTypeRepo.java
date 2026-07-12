package com.aishwarya.Finbank.repository;

import com.aishwarya.Finbank.model.PropertyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyTypeRepo extends JpaRepository<PropertyType, Long> {
    boolean existsByName(String value);
}
