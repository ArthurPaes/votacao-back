package com.example.pautachallenge.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "VoteStatus",
    description = "Status de um voto",
    example = "ABLE_TO_VOTE"
)
public enum VoteStatus {
    @Schema(description = "Usuário pode votar", example = "ABLE_TO_VOTE")
    ABLE_TO_VOTE,
    
    @Schema(description = "Usuário não pode votar", example = "UNABLE_TO_VOTE")
    UNABLE_TO_VOTE
} 