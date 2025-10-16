package com.backend.Meezan.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.jdbc.UncategorizedSQLException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle database constraint violations including stored procedure errors
    @ExceptionHandler({DataIntegrityViolationException.class, UncategorizedSQLException.class})
    public ResponseEntity<Object> handleDatabaseException(Exception ex) {

        // Traverse the exception chain to find the actual SQL message
        Throwable cause = ex;
        String sqlMessage = null;
        while (cause != null) {
            if (cause.getMessage() != null) {
                sqlMessage = cause.getMessage();
                // Stop if we find a relevant constraint message
                if (sqlMessage.toLowerCase().contains("chk_id_13_digits") ||
                        sqlMessage.toLowerCase().contains("violates check constraint") ||
                        sqlMessage.toLowerCase().contains("unique constraint")) {
                    break;
                }
            }
            cause = cause.getCause();
        }

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());

        // 13-digit ID violation
        if (sqlMessage != null && sqlMessage.toLowerCase().contains("chk_id_13_digits")) {
            body.put("status", HttpStatus.BAD_REQUEST.value());
            body.put("error", "Invalid User ID");
            body.put("message", "User ID must be exactly 13 digits long. Please provide a valid 13-digit numeric ID.");
            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        }

        // Other check constraints
        if (sqlMessage != null && sqlMessage.toLowerCase().contains("violates check constraint")) {
            body.put("status", HttpStatus.BAD_REQUEST.value());
            body.put("error", "Invalid Data");
            body.put("message", "Provided data violates database constraints. Please check your input.");
            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        }

        // Unique constraint violations (e.g., email already exists)
        if (sqlMessage != null && sqlMessage.toLowerCase().contains("unique constraint")) {
            body.put("status", HttpStatus.BAD_REQUEST.value());
            body.put("error", "Duplicate Data");
            body.put("message", "Duplicate entry found. Please ensure unique values for fields like email or ID.");
            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        }

        // Fallback for other database errors
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Database Error");
        body.put("message", sqlMessage != null ? sqlMessage : ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // Optional: generic handler for any unexpected exceptions
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
