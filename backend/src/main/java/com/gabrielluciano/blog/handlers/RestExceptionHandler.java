package com.gabrielluciano.blog.handlers;

import com.gabrielluciano.blog.error.ErrorDetails;
import com.gabrielluciano.blog.error.ValidationErrorDetails;
import com.gabrielluciano.blog.exceptions.ConstraintViolationException;
import com.gabrielluciano.blog.exceptions.InvalidCredentialsException;
import com.gabrielluciano.blog.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException ex,
                                                                        HttpServletRequest request) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .title("Could not find resource")
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(LocalDateTime.now(ZoneOffset.UTC))
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorDetails> handleConstraintViolationException(ConstraintViolationException ex,
                                                                           HttpServletRequest request) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .title("Constraint Violation Exception")
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now(ZoneOffset.UTC))
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorDetails> handleInvalidCredentialsException(InvalidCredentialsException ex,
                                                                          HttpServletRequest request) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .title("Invalid Credentials")
                .message(ex.getMessage())
                .status(HttpStatus.UNAUTHORIZED.value())
                .timestamp(LocalDateTime.now(ZoneOffset.UTC))
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        ServletWebRequest servletWebRequest = (ServletWebRequest) request;

        String fields = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getField)
                .collect(Collectors.joining(","));

        String fieldsMessages = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(","));

        ValidationErrorDetails validationErrorDetails = ValidationErrorDetails.builder()
                .title("Validation Error")
                .message("An invalid request body was sent to the server, please check fields and fieldsMessages attributes")
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now(ZoneOffset.UTC))
                .path(servletWebRequest.getRequest().getRequestURI())
                .fields(fields)
                .fieldsMessages(fieldsMessages)
                .build();

        return new ResponseEntity<>(validationErrorDetails, headers, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatusCode statusCode, WebRequest request) {

        ServletWebRequest servletWebRequest = (ServletWebRequest) request;

        String message = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();

        ErrorDetails errorDetails = ErrorDetails.builder()
                .title(ex.getMessage())
                .message(message)
                .status(statusCode.value())
                .timestamp(LocalDateTime.now(ZoneOffset.UTC))
                .path(servletWebRequest.getRequest().getRequestURI())
                .build();

        return new ResponseEntity<>(errorDetails, headers, statusCode);
    }
}
