package com.gabrielluciano.blog.error.exceptions;

import org.springframework.security.authentication.BadCredentialsException;

public class UserNotAllowedToModifyPostException extends BadCredentialsException {

    public UserNotAllowedToModifyPostException(Long id) {
        super("User with id '" + id + "' is not allowed to modify this post because it is not an admin or the author of this post");
    }
}
