package com.gabrielluciano.blog.error.handlers;

import com.gabrielluciano.blog.error.exceptions.InvalidCredentialsException;
import com.gabrielluciano.blog.error.exceptions.InvalidPostCommentStatusException;
import com.gabrielluciano.blog.error.exceptions.ResourceNotFoundException;
import com.gabrielluciano.blog.error.exceptions.UserNotAllowedToModifyResourceException;
import com.gabrielluciano.blog.error.models.ErrorDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> resourceNotFoundExceptionHandler(ResourceNotFoundException ex,
                                                                         HttpServletRequest request) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .title("Resource Not Found")
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now(ZoneOffset.UTC))
                .build();

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDetails> handleDataIntegrityViolationException(DataIntegrityViolationException ex,
                                                                              HttpServletRequest request) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .title("Operation violates database constraint")
                .status(HttpStatus.UNAUTHORIZED.value())
                .message(ex.getCause().getCause().getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now(ZoneOffset.UTC))
                .build();

        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorDetails> handleInvalidCredentialsException(InvalidCredentialsException ex,
                                                                          HttpServletRequest request) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .title("Invalid Credentials")
                .status(HttpStatus.UNAUTHORIZED.value())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now(ZoneOffset.UTC))
                .build();

        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserNotAllowedToModifyResourceException.class)
    public ResponseEntity<ErrorDetails> handleUserNotAllowedToModifyResourceException(UserNotAllowedToModifyResourceException ex,
                                                                                      HttpServletRequest request) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .title("User Not Allowed to Modify Resource")
                .status(HttpStatus.FORBIDDEN.value())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now(ZoneOffset.UTC))
                .build();

        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InvalidPostCommentStatusException.class)
    public ResponseEntity<ErrorDetails> handleInvalidPostCommentStatusException(InvalidPostCommentStatusException ex,
                                                                                HttpServletRequest request) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .title("Invalid Post Comment Status")
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now(ZoneOffset.UTC))
                .build();

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatusCode statusCode, WebRequest request) {

        ErrorDetails errorDetails = ErrorDetails.builder()
                .title(ex.getMessage())
                .status(statusCode.value())
                .message(ex.getCause().getMessage())
                .path(request.getContextPath())
                .timestamp(LocalDateTime.now(ZoneOffset.UTC))
                .build();

        return new ResponseEntity<>(errorDetails, statusCode);
    }
}
