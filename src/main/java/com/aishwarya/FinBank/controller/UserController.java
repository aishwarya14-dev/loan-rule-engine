package com.aishwarya.FinBank.controller;

import com.aishwarya.FinBank.model.User;
import com.aishwarya.FinBank.service.UserDetailsServiceImpl;
import com.aishwarya.FinBank.service.UserService;
import com.aishwarya.FinBank.utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

}
