package com.example.pautachallenge.infra;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class RequestsExceptionHandler {
    
    @ExceptionHandler(EntityNotFoundException.class)
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "400",
            description = "Entidade não encontrada",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ExceptionDTO.class),
                examples = {
                    @ExampleObject(
                        name = "Entidade Não Encontrada",
                        summary = "Entidade não encontrada",
                        description = "Erro quando uma entidade não é encontrada pelo ID",
                        value = """
                            {
                                "message": "Data not found with provided ID",
                                "status": 404
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ExceptionDTO> threat404(){
        ExceptionDTO response = new ExceptionDTO("Data not found with provided ID", 404);
        return ResponseEntity.badRequest().body(response);
    }
}
