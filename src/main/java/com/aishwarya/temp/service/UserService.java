package com.aishwarya.Finbank.service;
import com.aishwarya.Finbank.dto.response.UserResponseDto;
import com.aishwarya.Finbank.exceptions.DuplicateUserException;
import com.aishwarya.Finbank.exceptions.UserCreationException;
import com.aishwarya.Finbank.model.User;
import com.aishwarya.Finbank.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    private final static Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserResponseDto saveUser(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new DuplicateUserException(
                    "Username already exists");
        }

        if (userRepository.findByMobileNumber(user.getMobileNumber()) != null) {
            throw new DuplicateUserException(
                    "Mobile number already exists");
        }
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return new UserResponseDto(user.getUsername(), user.getMobileNumber());
        } catch (Exception e) {
            logger.error("error occurred for {} ", user.getUsername(), e);
            throw new UserCreationException(
                    "Failed to create user", e);
        }
    }
}
