package com.example.pautachallenge.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pautachallenge.domain.interfaces.UserLoginRequest;
import com.example.pautachallenge.domain.dto.UserResponseDTO;
import com.example.pautachallenge.service.UserService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints para autenticação de usuários")
public class AuthController {

    private final UserService userService;

    @PostMapping
    @Operation(
        summary = "Autenticar usuário",
        description = "Realiza a autenticação de um usuário no sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Autenticação realizada com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDTO.class),
                examples = @ExampleObject(
                    name = "Login bem-sucedido",
                    value = """
                        {
                          "id": 1,
                          "name": "João Silva",
                          "email": "joao@example.com"
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Credenciais inválidas",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Erro de autenticação",
                    value = """
                        {
                          "message": "Credenciais inválidas",
                          "error": "AUTHENTICATION_ERROR",
                          "timestamp": "2025-08-07T22:00:00"
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Erro de validação",
                    value = """
                        {
                          "message": "Email deve ser válido",
                          "error": "VALIDATION_ERROR",
                          "timestamp": "2025-08-07T22:00:00"
                        }
                        """
                )
            )
        )
    })
    public UserResponseDTO login(
        @Parameter(
            description = "Credenciais de login",
            required = true,
            content = @Content(
                examples = @ExampleObject(
                    name = "Credenciais",
                    value = """
                        {
                          "email": "joao@example.com",
                          "password": "senha123"
                        }
                        """
                )
            )
        )
        @Valid @RequestBody UserLoginRequest userBody
    ) {
        return userService.login(userBody);
    }
}
