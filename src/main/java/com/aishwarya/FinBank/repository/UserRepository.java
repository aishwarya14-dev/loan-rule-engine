package com.aishwarya.FinBank.repository;

import com.aishwarya.FinBank.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
    User findByUsername(String username);
}
