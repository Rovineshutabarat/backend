package com.chelly.backend.handler;

import com.chelly.backend.models.payload.response.ErrorResponse;
import com.chelly.backend.models.payload.response.SuccessResponse;
import com.chelly.backend.models.payload.response.ValidationErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

public class ResponseHandler {
    public static <T> ResponseEntity<SuccessResponse<T>> buildSuccessResponse(
            HttpStatus httpStatus,
            String message,
            T body) {
        return new ResponseEntity<>(
                SuccessResponse.<T>builder()
                        .status("Success")
                        .code(httpStatus.value())
                        .message(message)
                        .data(body)
                        .build(),
                httpStatus);
    }

    public static ResponseEntity<ErrorResponse> buildErrorResponse(
            HttpServletRequest request,
            HttpStatus httpStatus,
            String message) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .status("Error")
                        .code(httpStatus.value())
                        .message(message)
                        .path(request.getRequestURI())
                        .timestamp(new Date())
                        .build(),
                httpStatus);
    }

    public static ResponseEntity<ValidationErrorResponse> buildValidationErrorResponse(
            HttpServletRequest request,
            MethodArgumentNotValidException exception) {
        return new ResponseEntity<>(
                ValidationErrorResponse.builder()
                        .status("Validation Error")
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message("Validation Error")
                        .path(request.getRequestURI())
                        .timestamp(new Date())
                        .errors(extractErrors(exception))
                        .build(),
                HttpStatus.BAD_REQUEST);
    }

    private static Map<String, String> extractErrors(MethodArgumentNotValidException exception) {
        return exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .filter(error -> error.getDefaultMessage() != null)
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (ppk1, ppk2) -> ppk1)
                );
    }
}