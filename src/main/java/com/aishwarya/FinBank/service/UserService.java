package com.aishwarya.FinBank.service;

import com.aishwarya.FinBank.model.User;
import com.aishwarya.FinBank.repository.UserRepository;
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

    public void saveUser(User user){
        try{
            user.setPassword(passwordEncoder.encode(user.getPassword()));
             userRepository.save(user);
        }catch (Exception e){
            logger.error("error occured for {} ", user.getUsername(),e);
        }
    }


    public User verifyUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
