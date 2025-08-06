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
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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
        summary = "Criar novo usuário",
        description = """
            Cria um novo usuário no sistema com validações de:
            - Email único
            - CPF único e válido
            - Senha criptografada
            - Campos obrigatórios
            
            **Validações:**
            - Nome: mínimo 3 caracteres
            - Email: formato válido e único
            - CPF: formato brasileiro válido e único
            - Senha: mínimo 6 caracteres
            """,
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Dados do usuário a ser criado",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserDTO.class),
                examples = {
                    @ExampleObject(
                        name = "Usuário Válido",
                        summary = "Exemplo de criação de usuário válido",
                        description = "Exemplo de payload para criar um usuário com dados válidos",
                        value = """
                            {
                                "name": "João Silva",
                                "email": "joao.silva@example.com",
                                "cpf": "12345678909",
                                "password": "senha123"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Usuário com Dados Mínimos",
                        summary = "Exemplo com dados mínimos necessários",
                        description = "Exemplo de payload com o mínimo de dados necessários",
                        value = """
                            {
                                "name": "Ana",
                                "email": "ana@test.com",
                                "cpf": "98765432100",
                                "password": "123456"
                            }
                            """
                    )
                }
            )
        )
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Usuário criado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDTO.class),
                examples = {
                    @ExampleObject(
                        name = "Resposta de Sucesso",
                        summary = "Usuário criado",
                        description = "Resposta quando o usuário é criado com sucesso",
                        value = """
                            {
                                "id": 1,
                                "name": "João Silva",
                                "email": "joao.silva@example.com",
                                "cpf": "12345678909"
                            }
                            """
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos ou usuário já existe",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(description = "Mensagem de erro"),
                examples = {
                    @ExampleObject(
                        name = "Email Já Existe",
                        summary = "Email já cadastrado",
                        description = "Erro quando o email já está em uso",
                        value = """
                            {
                                "message": "Email já cadastrado",
                                "timestamp": "2024-01-15T10:30:00",
                                "status": 400
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "CPF Já Existe",
                        summary = "CPF já cadastrado",
                        description = "Erro quando o CPF já está em uso",
                        value = """
                            {
                                "message": "CPF já cadastrado",
                                "timestamp": "2024-01-15T10:30:00",
                                "status": 400
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Dados Inválidos",
                        summary = "Validação falhou",
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
        ),
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
    public UserResponseDTO createUser(
        @Parameter(
            description = "Dados do usuário a ser criado",
            required = true,
            schema = @Schema(implementation = UserDTO.class)
        )
        @Valid @RequestBody UserDTO userDTO
    ) {
        log.info("Recebendo requisição para criar novo usuário: {}", userDTO.getEmail());
        UserResponseDTO createdUser = userService.createUser(userDTO);
        log.info("Usuário criado com sucesso. ID: {}, Email: {}", createdUser.getId(), createdUser.getEmail());
        return createdUser;
    }
}
