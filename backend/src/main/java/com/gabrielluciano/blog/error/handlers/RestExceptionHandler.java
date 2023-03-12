package com.gabrielluciano.blog.error.handlers;

import com.gabrielluciano.blog.error.exceptions.ResourceNotFoundException;
import com.gabrielluciano.blog.error.models.ErrorDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> resourceNotFoundExceptionHandler(ResourceNotFoundException ex, HttpServletRequest request) {
        ErrorDetails errorDetails = new ErrorDetails()
                .withTitle("Resource Not Found")
                .withStatus(HttpStatus.NOT_FOUND.value())
                .withMessage(ex.getMessage())
                .withPath(request.getRequestURI());

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDetails> handleDataIntegrityViolationException(DataIntegrityViolationException ex,
                                                                              HttpServletRequest request) {
        ErrorDetails errorDetails = new ErrorDetails()
                .withTitle("Operation violates database constraint")
                .withStatus(HttpStatus.UNAUTHORIZED.value())
                .withMessage(ex.getCause().getCause().getMessage())
                .withPath(request.getRequestURI());

        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

}
