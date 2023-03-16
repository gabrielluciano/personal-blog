package com.gabrielluciano.blog.error.exceptions;

import org.springframework.security.authentication.BadCredentialsException;

public class UserNotAllowedToModifyResourceException extends BadCredentialsException {

    public UserNotAllowedToModifyResourceException(Long id) {
        super("User with id '" + id + "' is not allowed to modify this resource");
    }
}
