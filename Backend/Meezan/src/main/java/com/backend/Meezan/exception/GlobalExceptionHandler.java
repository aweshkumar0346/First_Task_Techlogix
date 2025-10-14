package com.backend.Meezan.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handles database constraint violations (like your 13-digit rule)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String message = ex.getMostSpecificCause().getMessage();

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());

        if (message != null && message.contains("id_digits_only")) {
            body.put("status", HttpStatus.BAD_REQUEST.value());
            body.put("error", "Invalid ID");
            body.put("message", "ID must be exactly 13 digits long.");
            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        }

        // default fallback for other constraint violations
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Database constraint violation");
        body.put("message", message);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // Optional: generic handler for unexpected errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Internal Server Error");
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
