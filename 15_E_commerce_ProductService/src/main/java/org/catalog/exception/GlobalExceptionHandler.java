package org.catalog.exception;

import org.catalog.dto.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(InValidDetailsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDetails(InValidDetailsException ex) {
    	
        log.warn("Invalid details: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse("ERR-1001", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCategoryNotFound(CategoryNotFoundException ex) {
    	
        log.warn("Category not found: ", ex.getMessage());
        ErrorResponse error = new ErrorResponse("ERR-1002", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        
    }

    

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Unexpected error occurred", ex);
        ErrorResponse error = new ErrorResponse("ERR-1000", "Something went wrong. Please try again later.");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}