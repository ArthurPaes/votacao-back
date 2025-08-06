package com.example.pautachallenge.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
    name = "VoteDTO",
    description = "Dados para registro de um voto",
    example = """
        {
            "sectionId": 1,
            "userId": 1,
            "vote": true
        }
        """
)
public class VoteDTO {

    @Schema(
        description = "ID da pauta onde o voto será registrado",
        example = "1"
    )
    @NotNull(message = "ID da pauta é obrigatório")
    private Long sectionId;

    @Schema(
        description = "ID do usuário que está votando",
        example = "1"
    )
    @NotNull(message = "ID do usuário é obrigatório")
    private Long userId;

    @Schema(
        description = "Voto do usuário (true = a favor, false = contra)",
        example = "true"
    )
    @NotNull(message = "Voto é obrigatório")
    private Boolean vote;
} 