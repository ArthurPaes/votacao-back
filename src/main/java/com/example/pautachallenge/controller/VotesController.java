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
@Tag(name = "Votos", description = "Endpoints para gerenciamento de votos")
public class VotesController {

    private final VotesService votesService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Registrar voto",
        description = "Registra um voto de um usuário em uma pauta específica"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Voto registrado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Votes.class),
                examples = @ExampleObject(
                    name = "Voto criado",
                    value = """
                        {
                          "id": 1,
                          "userId": 1,
                          "sectionId": 1,
                          "vote": true,
                          "status": "ABLE_TO_VOTE"
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos ou voto duplicado",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Erro de validação",
                    value = """
                        {
                          "message": "Esse usuário já votou nesta seção",
                          "error": "VALIDATION_ERROR",
                          "timestamp": "2025-08-07T22:00:00"
                        }
                        """
                )
            )
        )
    })
    public Votes createVote(
        @Parameter(
            description = "Dados do voto",
            required = true,
            content = @Content(
                examples = @ExampleObject(
                    name = "Voto a favor",
                    value = """
                        {
                          "sectionId": 1,
                          "userId": 1,
                          "vote": true
                        }
                        """
                )
            )
        )
        @Valid @RequestBody VoteDTO voteDTO
    ) {
        return votesService.createVote(voteDTO);
    }
}
