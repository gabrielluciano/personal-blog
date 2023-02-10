package com.gabrielluciano.blog.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class TagAlreadyExistsAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(TagAlreadyExistsException.class)
    public String tagAlreadyExistsHandler(TagAlreadyExistsException ex) {
        return ex.getMessage();
    }

}
