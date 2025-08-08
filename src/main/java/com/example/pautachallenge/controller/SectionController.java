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

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/section")
@RequiredArgsConstructor
@Tag(name = "Pautas", description = "Endpoints para gerenciamento de pautas e consulta de votos")
public class SectionController {

    private final SectionService sectionService;

    @GetMapping
    @Operation(
        summary = "Listar pautas",
        description = "Lista todas as pautas com contagem de votos para um usuário específico"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de pautas retornada com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SectionWithVotesCount.class),
                examples = @ExampleObject(
                    name = "Lista de pautas",
                    value = """
                        [
                          {
                            "id": 1,
                            "name": "Pauta Importante",
                            "description": "Descrição da pauta",
                            "totalVotes": 5,
                            "votesTrue": 3,
                            "votesFalse": 2,
                            "hasVoted": true,
                            "isExpired": false
                          }
                        ]
                        """
                )
            )
        )
    })
    public List<SectionWithVotesCount> getAllSections(
        @Parameter(
            description = "ID do usuário para verificar se já votou",
            required = true,
            example = "1"
        )
        @RequestParam Long userId
    ) {
        return sectionService.getAllSectionsWithVotes(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Criar pauta",
        description = "Cria uma nova pauta para votação"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Pauta criada com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Section.class),
                examples = @ExampleObject(
                    name = "Pauta criada",
                    value = """
                        {
                          "id": 1,
                          "name": "Pauta Importante",
                          "description": "Descrição da pauta",
                          "expiration": 10,
                          "start_at": "2025-08-07T22:00:00"
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
                          "message": "Nome deve ter entre 3 e 200 caracteres",
                          "error": "VALIDATION_ERROR",
                          "timestamp": "2025-08-07T22:00:00"
                        }
                        """
                )
            )
        )
    })
    public Section createSection(
        @Parameter(
            description = "Dados da pauta",
            required = true,
            content = @Content(
                examples = @ExampleObject(
                    name = "Nova pauta",
                    value = """
                        {
                          "name": "Pauta Importante",
                          "description": "Descrição detalhada da pauta",
                          "expiration": 10
                        }
                        """
                )
            )
        )
        @Valid @RequestBody SectionDTO sectionDTO
    ) {
        return sectionService.createSection(sectionDTO);
    }
}
