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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.pautachallenge.domain.dto.UserDTO;
import com.example.pautachallenge.domain.dto.UserResponseDTO;
import com.example.pautachallenge.service.UserService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários")
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Criar usuário",
        description = "Cria um novo usuário no sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Usuário criado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDTO.class),
                examples = @ExampleObject(
                    name = "Usuário criado",
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
            responseCode = "400",
            description = "Dados inválidos ou usuário já existe",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Erro de validação",
                    value = """
                        {
                          "message": "Email já cadastrado",
                          "error": "VALIDATION_ERROR",
                          "timestamp": "2025-08-07T22:00:00"
                        }
                        """
                )
            )
        )
    })
    public UserResponseDTO createUser(
        @Parameter(
            description = "Dados do usuário",
            required = true,
            content = @Content(
                examples = @ExampleObject(
                    name = "Novo usuário",
                    value = """
                        {
                          "name": "João Silva",
                          "cpf": "12345678909",
                          "password": "senha123",
                          "email": "joao@example.com"
                        }
                        """
                )
            )
        )
        @Valid @RequestBody UserDTO userDTO
    ) {
        return userService.createUser(userDTO);
    }
}
