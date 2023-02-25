package com.gabrielluciano.blog.exceptions;

public class InvalidEmailOrPasswordException extends RuntimeException {

    public InvalidEmailOrPasswordException() {
        super("Invalid email or password");
    }

}
