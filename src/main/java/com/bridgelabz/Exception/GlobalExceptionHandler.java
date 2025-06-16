package com.bridgelabz.Exception;

import com.bridgelabz.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TechStackNotFoundException.class)
    public ResponseEntity<ApiResponse> handleTechStackNotFound(TechStackNotFoundException ex) {
        return new ResponseEntity<>(new ApiResponse(false, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LearnerNotFoundException.class)
    public ResponseEntity<ApiResponse> handleLearnerNotFound(LearnerNotFoundException ex) {
        return new ResponseEntity<>(new ApiResponse(false, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GoogleFormNotFoundException.class)
    public ResponseEntity<ApiResponse> handleGoogleFormNotFound(GoogleFormNotFoundException ex) {
        return new ResponseEntity<>(new ApiResponse(false, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    // Catch-all for unexpected exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGlobalException(Exception ex) {
        return new ResponseEntity<>(new ApiResponse(false, "Internal server error: " + ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

