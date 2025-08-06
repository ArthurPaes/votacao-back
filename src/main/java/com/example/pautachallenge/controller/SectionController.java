package com.example.pautachallenge.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.pautachallenge.domain.dto.SectionDTO;
import com.example.pautachallenge.domain.interfaces.SectionWithVotesCount;
import com.example.pautachallenge.domain.model.Section;
import com.example.pautachallenge.service.SectionService;

@Slf4j
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/section")
@RequiredArgsConstructor
@Tag(name = "Pautas", description = "Endpoints para gerenciamento de pautas e consulta de votos")
public class SectionController {

    private final SectionService sectionService;

    @GetMapping
    @Operation(
        summary = "Listar todas as pautas com contagem de votos",
        description = """
            Retorna todas as pautas disponíveis no sistema com informações detalhadas sobre votos.
            
            **Informações Retornadas:**
            - Dados básicos da pauta (ID, nome, descrição, expiração)
            - Contagem total de votos
            - Contagem de votos a favor (true)
            - Contagem de votos contra (false)
            - Se o usuário já votou na pauta
            - Se a pauta está expirada
            
            **Parâmetros:**
            - `userId`: ID do usuário para verificar se já votou
            
            **Ordenação:**
            - Pautas são ordenadas por data de criação (mais recentes primeiro)
            
            **Filtros:**
            - Apenas pautas ativas são retornadas
            - Pautas expiradas são marcadas mas ainda aparecem na lista
            """,
        parameters = {
            @Parameter(
                name = "userId",
                description = "ID do usuário para verificar se já votou nas pautas",
                required = true,
                schema = @Schema(type = "long", example = "1"),
                example = "1"
            )
        }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de pautas retornada com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SectionWithVotesCount.class, type = "array"),
                examples = {
                    @ExampleObject(
                        name = "Lista com Pautas",
                        summary = "Lista com pautas existentes",
                        description = "Resposta quando existem pautas no sistema",
                        value = """
                            [
                                {
                                    "id": 1,
                                    "name": "Pauta Importante",
                                    "description": "Esta é uma descrição detalhada da pauta que será votada pelos associados",
                                    "expiration": 10,
                                    "start_at": "2024-01-15T10:00:00",
                                    "totalVotes": 5,
                                    "votesTrue": 3,
                                    "votesFalse": 2,
                                    "hasVoted": true,
                                    "isExpired": false
                                },
                                {
                                    "id": 2,
                                    "name": "Segunda Pauta",
                                    "description": "Outra pauta para votação",
                                    "expiration": 5,
                                    "start_at": "2024-01-15T09:00:00",
                                    "totalVotes": 0,
                                    "votesTrue": 0,
                                    "votesFalse": 0,
                                    "hasVoted": false,
                                    "isExpired": true
                                }
                            ]
                            """
                    ),
                    @ExampleObject(
                        name = "Lista Vazia",
                        summary = "Lista vazia",
                        description = "Resposta quando não existem pautas no sistema",
                        value = "[]"
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Parâmetros inválidos",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(description = "Mensagem de erro"),
                examples = {
                    @ExampleObject(
                        name = "UserId Inválido",
                        summary = "UserId inválido",
                        description = "Erro quando o userId não é um número válido",
                        value = """
                            {
                                "message": "userId deve ser um número válido",
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
    public List<SectionWithVotesCount> getAllSections(
        @Parameter(
            description = "ID do usuário para verificar se já votou nas pautas",
            required = true,
            schema = @Schema(type = "long", example = "1")
        )
        @RequestParam Long userId
    ) {
        log.info("Recebendo requisição para buscar todas as seções para o usuário: {}", userId);
        List<SectionWithVotesCount> sections = sectionService.getAllSectionsWithVotes(userId);
        log.info("Retornando {} seções para o usuário: {}", sections.size(), userId);
        return sections;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Criar nova pauta",
        description = """
            Cria uma nova pauta no sistema para votação.
            
            **Validações:**
            - Nome: mínimo 3 caracteres
            - Descrição: obrigatória
            - Expiração: deve ser um número positivo (em minutos)
            
            **Comportamento:**
            - A data de início é automaticamente definida como o momento atual
            - A pauta fica disponível para votação imediatamente
            - O tempo de expiração é calculado a partir da data de início
            
            **Observações:**
            - Pautas expiradas ainda podem ser consultadas
            - Votos em pautas expiradas são permitidos mas podem ser invalidados
            """,
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Dados da pauta a ser criada",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SectionDTO.class),
                examples = {
                    @ExampleObject(
                        name = "Pauta Completa",
                        summary = "Exemplo de criação de pauta completa",
                        description = "Exemplo de payload para criar uma pauta com todos os dados",
                        value = """
                            {
                                "name": "Pauta Importante",
                                "description": "Esta é uma descrição detalhada da pauta que será votada pelos associados",
                                "expiration": 10
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Pauta Simples",
                        summary = "Exemplo de criação de pauta simples",
                        description = "Exemplo de payload para criar uma pauta com dados mínimos",
                        value = """
                            {
                                "name": "Nova Pauta",
                                "description": "Descrição da nova pauta",
                                "expiration": 5
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
            description = "Pauta criada com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Section.class),
                examples = {
                    @ExampleObject(
                        name = "Resposta de Sucesso",
                        summary = "Pauta criada",
                        description = "Resposta quando a pauta é criada com sucesso",
                        value = """
                            {
                                "id": 1,
                                "name": "Pauta Importante",
                                "description": "Esta é uma descrição detalhada da pauta que será votada pelos associados",
                                "expiration": 10,
                                "start_at": "2024-01-15T10:30:00"
                            }
                            """
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(description = "Mensagem de erro"),
                examples = {
                    @ExampleObject(
                        name = "Nome Muito Curto",
                        summary = "Nome com menos de 3 caracteres",
                        description = "Erro quando o nome é muito curto",
                        value = """
                            {
                                "message": "Nome deve ter pelo menos 3 caracteres",
                                "timestamp": "2024-01-15T10:30:00",
                                "status": 400
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Expiração Inválida",
                        summary = "Expiração negativa ou zero",
                        description = "Erro quando a expiração é inválida",
                        value = """
                            {
                                "message": "Expiração deve ser maior que zero",
                                "timestamp": "2024-01-15T10:30:00",
                                "status": 400
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Descrição Obrigatória",
                        summary = "Descrição vazia",
                        description = "Erro quando a descrição está vazia",
                        value = """
                            {
                                "message": "Descrição é obrigatória",
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
    public Section createSection(
        @Parameter(
            description = "Dados da pauta a ser criada",
            required = true,
            schema = @Schema(implementation = SectionDTO.class)
        )
        @Valid @RequestBody SectionDTO sectionDTO
    ) {
        log.info("Recebendo requisição para criar nova seção: {}", sectionDTO.getName());
        
        Section section = new Section();
        section.setName(sectionDTO.getName());
        section.setDescription(sectionDTO.getDescription());
        section.setExpiration(sectionDTO.getExpiration());
        
        Section createdSection = sectionService.createSection(section);
        log.info("Seção criada com sucesso. ID: {}, Nome: {}", createdSection.getId(), createdSection.getName());
        return createdSection;
    }
}
