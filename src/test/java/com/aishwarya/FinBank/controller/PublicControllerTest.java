package com.aishwarya.FinBank.controller;

import com.aishwarya.FinBank.dto.response.UserResponseDto;
import com.aishwarya.FinBank.model.User;
import com.aishwarya.FinBank.service.UserDetailsServiceImpl;
import com.aishwarya.FinBank.service.UserService;
import com.aishwarya.FinBank.utility.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(PublicController.class)
public class PublicControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    public void testShouldCreateUserSuccessfully() throws Exception {
        User user = User.builder()
                .username("semblance@gmail.com")
                .password("password123")
                .role("USER")
                .mobileNumber("9876543210")
                .build();
        UserResponseDto response =
                new UserResponseDto("semblance@gmail.com", "9876543210");
        when(userService.saveUser(any(User.class)))
                .thenReturn(response);

        mockMvc.perform(post("/public/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("semblance@gmail.com"))
                .andExpect(jsonPath("$.mobileNumber").value("9876543210"));
    }

    @Test
    void shouldReturnBadRequestWhenNameMissing() throws Exception {
        User user = User.builder()
                .password("password123")
                .role("USER")
                .mobileNumber("9876543210")
                .build();

        mockMvc.perform(post("/public/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenMobileMissing() throws Exception {
        User user = User.builder()
                .username("semblance@gmail.com")
                .password("password123")
                .role("USER")
                .build();

        mockMvc.perform(post("/public/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testLoginUser() {
        // Implement test for user login
    }


}
