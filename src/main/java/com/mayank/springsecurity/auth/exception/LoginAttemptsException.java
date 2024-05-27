package com.mayank.springsecurity.auth.exception;

public class LoginAttemptsException extends RuntimeException{
    public LoginAttemptsException(String message) {
        super(message);
    }
}
