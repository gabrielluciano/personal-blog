package com.gabrielluciano.blog.exceptions;

public class UserIsNotAuthorException extends RuntimeException {

    public UserIsNotAuthorException(Long id) {
        super("User with id " + id + " is not an author");
    }

}
