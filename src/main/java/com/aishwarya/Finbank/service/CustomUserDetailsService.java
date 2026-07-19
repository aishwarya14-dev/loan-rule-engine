package com.aishwarya.Finbank.service;


import com.aishwarya.Finbank.model.User;
import com.aishwarya.Finbank.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email)  {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return new CustomUserDetails(user);
        }
        log.error("User not found with email: {}", email);
        throw new UsernameNotFoundException("User not found "+email);
    }
}

