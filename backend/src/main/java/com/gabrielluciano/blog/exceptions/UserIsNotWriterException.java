package com.gabrielluciano.blog.exceptions;

public class UserIsNotWriterException extends RuntimeException {

    public UserIsNotWriterException(Long id) {
        super("User with id " + id + " is not a writer");
    }

}
