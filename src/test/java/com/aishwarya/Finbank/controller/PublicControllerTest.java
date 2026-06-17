package com.aishwarya.Finbank.controller;
import com.aishwarya.FinBank.utility.JwtUtil;
import com.aishwarya.Finbank.controller.PublicController;
import com.aishwarya.Finbank.dto.response.UserResponseDto;
import com.aishwarya.Finbank.model.User;
import com.aishwarya.Finbank.service.UserDetailsServiceImpl;
import com.aishwarya.Finbank.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
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
    void shouldReturnJwtWhenCredentialsAreValid() throws Exception{
        User user = User.builder()
                .username("semblance@gmail.com")
                .password("password123")
                .role("USER")
                .mobileNumber("9876543210")
                .build();
        when(jwtUtil.generateToken(user.getUsername())).thenReturn("0febrwFEMCdaftqz3u/im5JKJOAmKGo8Fbyr/r3WA0Q=");
        String request = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/public/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isOk());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil).generateToken("semblance@gmail.com");
    }

    @Test
    public void testLoginUserShouldThrowExceptionWhenIncorrectUsername() {
        User user = User.builder()
                .username("abcxyz")
                .password("password123")
                .role("USER")
                .mobileNumber("9876543210")
                .build();

        when(authenticationManager.authenticate(any())).thenThrow(new RuntimeException());
    }

    @Test
    public void testLoginUserShouldThrowExceptionWhenIncorrectPassword() {
        User user = User.builder()
                .username("semblance@gmail.com")
                .password("xxxxx")
                .role("USER")
                .mobileNumber("9876543210")
                .build();

        when(authenticationManager.authenticate(any())).thenThrow(new RuntimeException());
    }


}
