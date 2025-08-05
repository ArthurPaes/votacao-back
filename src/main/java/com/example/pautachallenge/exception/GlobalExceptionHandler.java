package com.example.pautachallenge.exception;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "400",
            description = "Dados de entrada inválidos",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(description = "Mensagem de erro de validação"),
                examples = {
                    @ExampleObject(
                        name = "Validação Falhou",
                        summary = "Dados inválidos",
                        description = "Erro quando os dados não passam na validação",
                        value = """
                            {
                                "message": "Nome deve ter pelo menos 3 caracteres",
                                "timestamp": "2024-01-15T10:30:00",
                                "status": 400
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("Erro de validação: {}", ex.getMessage());
        
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getBindingResult().getFieldError().getDefaultMessage());
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
        
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "400",
            description = "Argumento inválido",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(description = "Mensagem de erro de argumento"),
                examples = {
                    @ExampleObject(
                        name = "Argumento Inválido",
                        summary = "Argumento inválido",
                        description = "Erro quando um argumento é inválido",
                        value = """
                            {
                                "message": "Credenciais inválidas!",
                                "timestamp": "2024-01-15T10:30:00",
                                "status": 400
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Argumento inválido: {}", ex.getMessage());
        
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
        
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "500",
            description = "Erro interno do servidor",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(description = "Mensagem de erro interno"),
                examples = {
                    @ExampleObject(
                        name = "Erro Interno",
                        summary = "Erro interno do servidor",
                        description = "Erro quando ocorre uma exceção não tratada",
                        value = """
                            {
                                "message": "Erro interno do servidor",
                                "timestamp": "2024-01-15T10:30:00",
                                "status": 500
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        log.error("Erro inesperado: {}", ex.getMessage(), ex);
        
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", "Erro interno do servidor");
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
} 