package com.example.pautachallenge.domain.dto;

import jakarta.validation.constraints.NotNull;

public record VoteDTO(
    @NotNull(message = "ID da seção é obrigatório")
    Long sectionId,
    
    @NotNull(message = "ID do usuário é obrigatório")
    Long userId,
    
    @NotNull(message = "Voto é obrigatório")
    Boolean vote
) {} 