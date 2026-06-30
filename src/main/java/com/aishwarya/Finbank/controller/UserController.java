package com.aishwarya.Finbank.controller;


import com.aishwarya.Finbank.utility.JwtUtil;
import com.aishwarya.Finbank.dto.response.UserResponseDto;
import com.aishwarya.Finbank.model.User;
import com.aishwarya.Finbank.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    private AuthenticationManager authenticationManager;

    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody User user) {
        UserResponseDto userResponseDto = userService.saveUser(user);
        return new ResponseEntity<>(userResponseDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        String jwt = jwtUtil.generateToken(user.getUsername());
        return new ResponseEntity<>(jwt, HttpStatus.OK);
    }
}
