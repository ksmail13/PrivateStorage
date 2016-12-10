package com.cloud.util.exception;

/**
 * Created by micky on 11/22/16.
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {
        super(String.format("%s not found", username));
    }
}
