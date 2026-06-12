package com.aishwarya.FinBank.service;


import com.aishwarya.FinBank.dto.response.UserResponseDto;
import com.aishwarya.FinBank.exceptions.DuplicateUserException;
import com.aishwarya.FinBank.exceptions.UserCreationException;
import com.aishwarya.FinBank.model.User;
import com.aishwarya.FinBank.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    User user;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    @BeforeEach
    public void setUp() {
        // Initialize any required objects or mock behavior here
         user = User.builder()
                .username("semblance@gmail.com")
                .password("password123")
                .role("USER")
                .mobileNumber("9876543210")
                .build();
    }

    @Test
    public void testShouldCreateUserSuccessfully() {
        when(userRepository.findByUsername(user.getUsername()))
                .thenReturn(null);
        when(userRepository.findByMobileNumber(user.getMobileNumber()))
                .thenReturn(null);

        when(userRepository.save(any(User.class)))
                .thenReturn(user);
        UserResponseDto userResponseDto = userService.saveUser(user);
        assertNotNull(userResponseDto);
        assertEquals("semblance@gmail.com", userResponseDto.getUsername());
        assertEquals("9876543210", userResponseDto.getMobileNumber());
    }

    @Test
    public void testShouldNotCreateUserWithDuplicateUsername() {
        when(userRepository.findByUsername(user.getUsername()))
                .thenReturn(user);
        assertThrows(DuplicateUserException.class, () -> userService.saveUser(user));
    }

    @Test
    public void testShouldNotCreateUserWithDuplicateMobileNumber() {
        when(userRepository.findByMobileNumber(user.getMobileNumber()))
                .thenReturn(user);
         assertThrows(DuplicateUserException.class, () -> userService.saveUser(user));
    }

    @Test
    void shouldThrowUserCreationExceptionWhenSaveFails() {
        when(userRepository.findByUsername(user.getUsername()))
                .thenReturn(null);
        when(userRepository.findByMobileNumber(user.getMobileNumber()))
                .thenReturn(null);
        when(userRepository.save(any(User.class)))
                .thenThrow(new RuntimeException("Database error"));

        assertThrows(UserCreationException.class, () -> userService.saveUser(user));

    }

    @Test
    public void testLoginUser() {
        // Implement test for user login
    }
}
