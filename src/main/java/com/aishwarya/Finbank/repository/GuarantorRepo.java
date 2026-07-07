package com.aishwarya.Finbank.repository;

import com.aishwarya.Finbank.model.Guarantor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuarantorRepo extends JpaRepository<Guarantor, Long> {
}
