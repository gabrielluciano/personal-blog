package com.gabrielluciano.blog.handlers;

import com.gabrielluciano.blog.error.ErrorDetails;
import com.gabrielluciano.blog.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

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

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatusCode statusCode, WebRequest request) {

        ServletWebRequest servletWebRequest = (ServletWebRequest) request;

        ErrorDetails errorDetails = ErrorDetails.builder()
                .title(ex.getMessage())
                .message(ex.getCause().getMessage())
                .status(statusCode.value())
                .timestamp(LocalDateTime.now(ZoneOffset.UTC))
                .path(servletWebRequest.getRequest().getRequestURI())
                .build();

        return new ResponseEntity<>(errorDetails, headers, statusCode);
    }
}
