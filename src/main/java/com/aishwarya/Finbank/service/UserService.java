package com.aishwarya.Finbank.service;
import com.aishwarya.Finbank.dto.response.UserResponseDto;
import com.aishwarya.Finbank.exceptions.DuplicateUserException;
import com.aishwarya.Finbank.exceptions.UserCreationException;
import com.aishwarya.Finbank.model.User;
import com.aishwarya.Finbank.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    public UserResponseDto saveUser(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            log.error("Username already exists for {} ", user.getUsername());
            throw new DuplicateUserException(
                    "Username already exists");
        }

        if (userRepository.findByMobileNumber(user.getMobileNumber()) != null) {
            log.error("Mobile number already exists for {} ", user.getMobileNumber());
            throw new DuplicateUserException(
                    "Mobile number already exists");
        }
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            log.info("User created successfully for {} ", user.getUsername());
            return new UserResponseDto(user.getUsername(), user.getMobileNumber());
        } catch (Exception e) {
            log.error("error occurred for {} ", user.getUsername(), e);
            throw new UserCreationException(
                    "Failed to create user", e);
        }
    }
}
