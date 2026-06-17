package com.aishwarya.Finbank.repository;


import com.aishwarya.Finbank.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);

    User findByMobileNumber(String mobileNumber);
}
