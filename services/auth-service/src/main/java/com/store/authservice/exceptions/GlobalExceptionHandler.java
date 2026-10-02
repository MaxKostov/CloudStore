package com.store.authservice.exceptions;

import com.store.authservice.dto.responses.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentialsException(
            InvalidCredentialsException ex,
            HttpServletRequest request) {

        ErrorResponse response = new ErrorResponse();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setError(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        response.setMessage(ex.getMessage());
        response.setPath(request.getRequestURI());
        response.setTimestamp(Instant.now());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(
            UserAlreadyExistsException ex,
            HttpServletRequest request) {

        ErrorResponse response = new ErrorResponse();
        response.setStatus(HttpStatus.CONFLICT.value());
        response.setError(HttpStatus.CONFLICT.getReasonPhrase());
        response.setMessage(ex.getMessage());
        response.setPath(request.getRequestURI());
        response.setTimestamp(Instant.now());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(SessionDoesntExistException.class)
    public ResponseEntity<ErrorResponse> handleSessionDoesntExistException(
            SessionDoesntExistException ex,
            HttpServletRequest request) {

        ErrorResponse response = new ErrorResponse();
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
        response.setMessage(ex.getMessage());
        response.setPath(request.getRequestURI());
        response.setTimestamp(Instant.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(
            Exception ex, HttpServletRequest request) {

        ErrorResponse response = new ErrorResponse();
        response.setStatus(500);
        response.setError("Internal Server Error");
        response.setMessage("An unexpected error occurred");
        response.setPath(request.getRequestURI());
        response.setTimestamp(Instant.now());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
