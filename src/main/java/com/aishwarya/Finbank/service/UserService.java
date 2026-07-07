package com.aishwarya.Finbank.service;
import com.aishwarya.Finbank.dto.user.UserResponseDto;
import com.aishwarya.Finbank.exceptions.DuplicateUserException;
import com.aishwarya.Finbank.exceptions.UserCreationException;
import com.aishwarya.Finbank.model.User;
import com.aishwarya.Finbank.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
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
        if (userRepository.findByEmail(user.getEmail()) != null) {
            log.error("Username already exists for {} ", user.getEmail());
            throw new DuplicateUserException(
                    "Username already exists");
        }

        if (userRepository.findByPhone(user.getPhone()) != null) {
            log.error("Mobile number already exists for {} ", user.getPhone());
            throw new DuplicateUserException(
                    "Mobile number already exists");
        }
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            log.info("User created successfully for {} ", user.getEmail());
            return new UserResponseDto(user.getEmail(), user.getPhone());
        } catch (Exception e) {
            log.error("error occurred for {} ", user.getEmail(), e);
            throw new UserCreationException(
                    "Failed to create user", e);
        }
    }
}
