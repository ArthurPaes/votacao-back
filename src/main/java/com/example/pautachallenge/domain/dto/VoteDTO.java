package com.example.pautachallenge.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoteDTO {
    
    @NotNull(message = "ID da seção é obrigatório")
    private Long sectionId;
    
    @NotNull(message = "ID do usuário é obrigatório")
    private Long userId;
    
    @NotNull(message = "Voto é obrigatório")
    private Boolean vote;
} 