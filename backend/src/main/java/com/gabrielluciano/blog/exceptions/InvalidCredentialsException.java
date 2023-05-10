package com.gabrielluciano.blog.exceptions;

public class InvalidCredentialsException extends RuntimeException{

    private static final String DEFAULT_MESSAGE = "Authentication failed: Incorrect email or password. Please try again.";

    public InvalidCredentialsException() {
        super(DEFAULT_MESSAGE);
    }

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
