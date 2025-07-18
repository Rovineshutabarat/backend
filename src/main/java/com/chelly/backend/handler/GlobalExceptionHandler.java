package com.chelly.backend.handler;

import com.chelly.backend.models.exceptions.AuthException;
import com.chelly.backend.models.exceptions.DuplicateElementException;
import com.chelly.backend.models.exceptions.ResourceNotFoundException;
import com.chelly.backend.models.payload.response.ErrorResponse;
import com.chelly.backend.models.payload.response.ValidationErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request) {
        return ResponseHandler.buildValidationErrorResponse(request, exception);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            HttpServletRequest request,
            ResourceNotFoundException exception) {
        return ResponseHandler.buildErrorResponse(
                request,
                HttpStatus.NOT_FOUND,
                exception.getMessage());
    }

    @ExceptionHandler(DuplicateElementException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateElementException(DuplicateElementException exception, HttpServletRequest request) {
        return ResponseHandler.buildErrorResponse(
                request,
                HttpStatus.CONFLICT,
                exception.getMessage());
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponse> handleAuthException(AuthException exception, HttpServletRequest request) {
        return ResponseHandler.buildErrorResponse(
                request,
                HttpStatus.UNAUTHORIZED,
                exception.getMessage());
    }

}
