package com.aishwarya.Finbank.repository;

import com.aishwarya.Finbank.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepo extends JpaRepository<Region, Long> {
    boolean existsByRegionName(String value);
}
