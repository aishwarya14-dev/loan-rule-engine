package com.aishwarya.Finbank.controller;


import com.aishwarya.Finbank.utility.JwtUtil;
import com.aishwarya.Finbank.dto.user.UserResponseDto;
import com.aishwarya.Finbank.model.User;
import com.aishwarya.Finbank.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/user")
@AllArgsConstructor
@Tag(name = "user APIs" , description = "Register and Login user")
public class UserController {

    private UserService userService;

    private AuthenticationManager authenticationManager;

    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody User user) {
        log.info("POST /register - username={}, mobile={}", user.getEmail(), user.getPhone());
        UserResponseDto userResponseDto = userService.saveUser(user);
        return new ResponseEntity<>(userResponseDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        log.info("POST /login - username={}",  user.getEmail());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        String jwt = jwtUtil.generateToken(user.getEmail());
        return new ResponseEntity<>(jwt, HttpStatus.OK);
    }
}
