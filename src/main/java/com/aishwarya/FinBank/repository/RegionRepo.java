package com.aishwarya.FinBank.repository;

import com.aishwarya.FinBank.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepo extends JpaRepository<Region, Long> {
    boolean existsByRegionCode(String value);
}
