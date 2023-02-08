package com.gabrielluciano.blog.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CategoryHasPostsAdivce {

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(CategoryHasPostsException.class)
    public String categoryHasPostsHandler(CategoryHasPostsException ex) {
        return ex.getMessage();
    }

}
