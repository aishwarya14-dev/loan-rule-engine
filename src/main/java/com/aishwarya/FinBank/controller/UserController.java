package com.aishwarya.FinBank.controller;

import com.aishwarya.FinBank.model.User;
import com.aishwarya.FinBank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

   @PostMapping("/register")
   public User register(@RequestBody User user){
       return userService.saveUser(user);
   }

    @PostMapping("/login")
    public User login(@RequestBody User user){
        return userService.saveUser(user);
    }
}
