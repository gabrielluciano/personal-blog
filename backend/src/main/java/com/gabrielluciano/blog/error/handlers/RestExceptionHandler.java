package com.gabrielluciano.blog.error.handlers;

import com.gabrielluciano.blog.error.exceptions.InvalidCredentialsException;
import com.gabrielluciano.blog.error.exceptions.InvalidPostCommentStatusException;
import com.gabrielluciano.blog.error.exceptions.ResourceNotFoundException;
import com.gabrielluciano.blog.error.exceptions.UserNotAllowedToModifyPostException;
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
    public ResponseEntity<ErrorDetails> resourceNotFoundExceptionHandler(ResourceNotFoundException ex,
                                                                         HttpServletRequest request) {
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

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorDetails> handleInvalidCredentialsException(InvalidCredentialsException ex,
                                                                          HttpServletRequest request) {
        ErrorDetails errorDetails = new ErrorDetails()
                .withTitle("Invalid Credentials")
                .withStatus(HttpStatus.UNAUTHORIZED.value())
                .withMessage(ex.getMessage())
                .withPath(request.getRequestURI());

        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserNotAllowedToModifyPostException.class)
    public ResponseEntity<ErrorDetails> handleUserNotAllowedToModifyPostException(UserNotAllowedToModifyPostException ex,
                                                                                  HttpServletRequest request) {
        ErrorDetails errorDetails = new ErrorDetails()
                .withTitle("User Not Allowed to Modify Post")
                .withStatus(HttpStatus.UNAUTHORIZED.value())
                .withMessage(ex.getMessage())
                .withPath(request.getRequestURI());

        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidPostCommentStatusException.class)
    public ResponseEntity<ErrorDetails> handleInvalidPostCommentStatusException(InvalidPostCommentStatusException ex,
                                                                                HttpServletRequest request) {
        ErrorDetails errorDetails = new ErrorDetails()
                .withTitle("Invalid Post Comment Status")
                .withStatus(HttpStatus.BAD_REQUEST.value())
                .withMessage(ex.getMessage())
                .withPath(request.getRequestURI());

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
