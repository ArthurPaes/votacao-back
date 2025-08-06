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

import com.example.pautachallenge.domain.dto.VoteDTO;
import com.example.pautachallenge.domain.model.Votes;
import com.example.pautachallenge.service.VotesService;

@Slf4j
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/votes")
@RequiredArgsConstructor
@Tag(name = "Votos", description = "Endpoints para registro e gerenciamento de votos")
public class VotesController {

    private final VotesService votesService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Registrar voto em uma pauta",
        description = """
            Registra um voto de um usuário em uma pauta específica.
            
            **Validações Realizadas:**
            - Verifica se o usuário existe
            - Verifica se a pauta existe
            - Verifica se o usuário já votou na pauta (voto único)
            - Valida o CPF do usuário (deve ser válido)
            - Verifica se a pauta não está expirada
            
            **Processo de Votação:**
            1. Validação do CPF do usuário
            2. Verificação de duplicidade de voto
            3. Verificação de expiração da pauta
            4. Registro do voto no sistema
            
            **Tipos de Voto:**
            - `true`: Voto a favor
            - `false`: Voto contra
            
            **Observações Importantes:**
            - Cada usuário pode votar apenas uma vez por pauta
            - Votos em pautas expiradas podem ser rejeitados
            - O CPF é validado automaticamente
            - Logs detalhados são gerados para auditoria
            """,
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Dados do voto a ser registrado",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = VoteDTO.class),
                examples = {
                    @ExampleObject(
                        name = "Voto a Favor",
                        summary = "Exemplo de voto a favor",
                        description = "Exemplo de payload para votar a favor de uma pauta",
                        value = """
                            {
                                "sectionId": 1,
                                "userId": 1,
                                "vote": true
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Voto Contra",
                        summary = "Exemplo de voto contra",
                        description = "Exemplo de payload para votar contra uma pauta",
                        value = """
                            {
                                "sectionId": 1,
                                "userId": 2,
                                "vote": false
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Voto em Pauta Diferente",
                        summary = "Exemplo de voto em pauta diferente",
                        description = "Exemplo de payload para votar em uma pauta diferente",
                        value = """
                            {
                                "sectionId": 2,
                                "userId": 1,
                                "vote": true
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
            description = "Voto registrado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Votes.class),
                examples = {
                    @ExampleObject(
                        name = "Resposta de Sucesso",
                        summary = "Voto registrado",
                        description = "Resposta quando o voto é registrado com sucesso",
                        value = """
                            {
                                "id": 1,
                                "sectionId": 1,
                                "userId": 1,
                                "vote": true,
                                "status": "ABLE_TO_VOTE"
                            }
                            """
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos ou voto duplicado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(description = "Mensagem de erro"),
                examples = {
                    @ExampleObject(
                        name = "Voto Duplicado",
                        summary = "Usuário já votou na pauta",
                        description = "Erro quando o usuário tenta votar duas vezes na mesma pauta",
                        value = """
                            {
                                "message": "Usuário já votou nesta pauta",
                                "timestamp": "2024-01-15T10:30:00",
                                "status": 400
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Pauta Não Encontrada",
                        summary = "Pauta inexistente",
                        description = "Erro quando a pauta especificada não existe",
                        value = """
                            {
                                "message": "Pauta não encontrada",
                                "timestamp": "2024-01-15T10:30:00",
                                "status": 400
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Usuário Não Encontrado",
                        summary = "Usuário inexistente",
                        description = "Erro quando o usuário especificado não existe",
                        value = """
                            {
                                "message": "Usuário não encontrado",
                                "timestamp": "2024-01-15T10:30:00",
                                "status": 400
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "CPF Inválido",
                        summary = "CPF do usuário inválido",
                        description = "Erro quando o CPF do usuário não é válido",
                        value = """
                            {
                                "message": "CPF inválido para votação",
                                "timestamp": "2024-01-15T10:30:00",
                                "status": 400
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Pauta Expirada",
                        summary = "Pauta já expirou",
                        description = "Erro quando a pauta já expirou",
                        value = """
                            {
                                "message": "Pauta expirada",
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
                                "message": "sectionId é obrigatório",
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
    public Votes createVote(
        @Parameter(
            description = "Dados do voto a ser registrado",
            required = true,
            schema = @Schema(implementation = VoteDTO.class)
        )
        @Valid @RequestBody VoteDTO voteDTO
    ) {
        log.info("Recebendo requisição para registrar voto. Pauta: {}, Usuário: {}, Voto: {}", 
                voteDTO.getSectionId(), voteDTO.getUserId(), voteDTO.getVote());
        
        Votes vote = new Votes();
        vote.setSectionId(voteDTO.getSectionId());
        vote.setUserId(voteDTO.getUserId());
        vote.setVote(voteDTO.getVote());
        
        Votes createdVote = votesService.createVote(vote);
        log.info("Voto registrado com sucesso. ID: {}, Pauta: {}, Usuário: {}, Voto: {}", 
                createdVote.getId(), createdVote.getSectionId(), createdVote.getUserId(), createdVote.getVote());
        return createdVote;
    }
}
