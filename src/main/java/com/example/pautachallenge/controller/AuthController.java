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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pautachallenge.domain.interfaces.UserLoginRequest;
import com.example.pautachallenge.domain.dto.UserResponseDTO;
import com.example.pautachallenge.service.UserService;

@Slf4j
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
        description = """
            Autentica um usuário no sistema usando email e senha.
            
            **Processo de Autenticação:**
            1. Valida se o usuário existe pelo email
            2. Verifica se a senha está correta (comparação com hash BCrypt)
            3. Retorna os dados do usuário autenticado
            
            **Segurança:**
            - Senhas são comparadas usando BCrypt
            - Não retorna a senha na resposta
            - Logs de tentativas de login são registrados
            
            **Observações:**
            - Este endpoint não gera tokens JWT
            - A autenticação é baseada em sessão
            - Para produção, considere implementar JWT
            """,
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Credenciais de login",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserLoginRequest.class),
                examples = {
                    @ExampleObject(
                        name = "Login Válido",
                        summary = "Exemplo de login válido",
                        description = "Exemplo de payload para login com credenciais válidas",
                        value = """
                            {
                                "email": "joao.silva@example.com",
                                "password": "senha123"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Login com Email Diferente",
                        summary = "Exemplo com email alternativo",
                        description = "Exemplo de payload com email diferente",
                        value = """
                            {
                                "email": "maria.santos@example.com",
                                "password": "minhasenha456"
                            }
                            """
                    )
                }
            )
        )
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Login realizado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDTO.class),
                examples = {
                    @ExampleObject(
                        name = "Resposta de Sucesso",
                        summary = "Usuário autenticado",
                        description = "Resposta quando o login é realizado com sucesso",
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
            description = "Credenciais inválidas",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(description = "Mensagem de erro de autenticação"),
                examples = {
                    @ExampleObject(
                        name = "Usuário Não Encontrado",
                        summary = "Email não cadastrado",
                        description = "Erro quando o email não existe no sistema",
                        value = """
                            {
                                "message": "Credenciais inválidas!",
                                "timestamp": "2024-01-15T10:30:00",
                                "status": 400
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Senha Incorreta",
                        summary = "Senha incorreta",
                        description = "Erro quando a senha está incorreta",
                        value = """
                            {
                                "message": "Credenciais inválidas!",
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
                                "message": "Email deve ser um endereço válido",
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
    public UserResponseDTO login(
        @Parameter(
            description = "Credenciais de login (email e senha)",
            required = true,
            schema = @Schema(implementation = UserLoginRequest.class)
        )
        @Valid @RequestBody UserLoginRequest userBody
    ) {
        log.info("Tentativa de login para o email: {}", userBody.getEmail());
        userService.authenticate(userBody.getEmail(), userBody.getPassword());
        log.info("Login realizado com sucesso para o email: {}", userBody.getEmail());
        UserResponseDTO user = userService.findUser(userBody.getEmail());
        log.debug("Retornando dados do usuário: {}", user.getEmail());
        return user;
    }
}
