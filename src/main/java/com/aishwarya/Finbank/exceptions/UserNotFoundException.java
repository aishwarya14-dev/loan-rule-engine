package com.aishwarya.Finbank.exceptions;


public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String userId) {
        super("user not found " + userId);
    }
}