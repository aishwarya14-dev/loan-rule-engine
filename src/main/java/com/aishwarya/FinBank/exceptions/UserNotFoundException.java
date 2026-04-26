package com.aishwarya.FinBank.exceptions;


public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String userId) {
        super("user not found " + userId);
    }
}