package com.example.pautachallenge.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.example.pautachallenge.infra.ErrorResponse;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getBindingResult().getFieldError().getDefaultMessage());
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
        
        String description = request != null ? request.getDescription(false) : "Unknown request";
        return new ErrorResponse(ex.getBindingResult().getFieldError().getDefaultMessage(), "VALIDATION_ERROR", description);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException e, WebRequest request) {
        String description = request != null ? request.getDescription(false) : "Unknown request";
        log.warn("IllegalArgumentException capturada: {} - Request: {}", e.getMessage(), description);
        return new ErrorResponse(e.getMessage(), "VALIDATION_ERROR", description);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGenericException(Exception e, WebRequest request) {
        String description = request != null ? request.getDescription(false) : "Unknown request";
        log.error("Exceção genérica capturada: {} - Request: {}", e.getMessage(), description, e);
        return new ErrorResponse("Erro interno do servidor", "INTERNAL_ERROR", description);
    }
} 