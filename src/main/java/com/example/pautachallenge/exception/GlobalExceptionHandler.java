package com.example.pautachallenge.exception;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.example.pautachallenge.infra.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        String errorMessage = "Erro de validação: " + errors.toString();
        log.warn("Erro de validação capturado: {} - Request: {}", errorMessage, request.getDescription(false));
        
        ErrorResponse errorResponse = new ErrorResponse(errorMessage, "VALIDATION_ERROR", request.getDescription(false));
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e, WebRequest request) {
        log.warn("IllegalArgumentException capturada: {} - Request: {}", e.getMessage(), request.getDescription(false));
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), "VALIDATION_ERROR", request.getDescription(false));
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e, WebRequest request) {
        log.error("Exceção genérica capturada: {} - Request: {}", e.getMessage(), request.getDescription(false), e);
        ErrorResponse errorResponse = new ErrorResponse("Erro interno do servidor", "INTERNAL_ERROR", request.getDescription(false));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
} 