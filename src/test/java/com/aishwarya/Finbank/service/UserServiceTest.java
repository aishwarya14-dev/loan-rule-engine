package com.aishwarya.Finbank.service;
import com.aishwarya.Finbank.dto.user.UserResponseDto;
import com.aishwarya.Finbank.enums.Role;
import com.aishwarya.Finbank.exceptions.DuplicateUserException;
import com.aishwarya.Finbank.exceptions.UserCreationException;
import com.aishwarya.Finbank.model.User;
import com.aishwarya.Finbank.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
                .email("semblance@gmail.com")
                .password("password123")
                .role(Role.USER)
                .phone("9876543210")
                .build();
    }

    @Test
    public void testShouldCreateUserSuccessfully() {
        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(null);
        when(userRepository.findByPhone(user.getPhone()))
                .thenReturn(null);

        when(userRepository.save(any(User.class)))
                .thenReturn(user);
        UserResponseDto userResponseDto = userService.saveUser(user);
        assertNotNull(userResponseDto);
        assertEquals("semblance@gmail.com", userResponseDto.getEmail());
        assertEquals("9876543210", userResponseDto.getPhone());
    }

    @Test
    public void testShouldNotCreateUserWithDuplicateUsername() {
        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(user);
        assertThrows(DuplicateUserException.class, () -> userService.saveUser(user));
    }

    @Test
    public void testShouldNotCreateUserWithDuplicateMobileNumber() {
        when(userRepository.findByPhone(user.getPhone()))
                .thenReturn(user);
         assertThrows(DuplicateUserException.class, () -> userService.saveUser(user));
    }

    @Test
    void shouldThrowUserCreationExceptionWhenSaveFails() {
        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(null);
        when(userRepository.findByPhone(user.getPhone()))
                .thenReturn(null);
        when(userRepository.save(any(User.class)))
                .thenThrow(new RuntimeException("Database error"));

        assertThrows(UserCreationException.class, () -> userService.saveUser(user));

    }
}
