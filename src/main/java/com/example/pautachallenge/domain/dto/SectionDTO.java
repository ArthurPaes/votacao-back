package com.example.pautachallenge.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
    name = "SectionDTO",
    description = "Dados para criação de uma nova pauta",
    example = """
        {
            "name": "Pauta Importante",
            "description": "Esta é uma descrição detalhada da pauta que será votada pelos associados",
            "expiration": 10
        }
        """
)
public class SectionDTO {

    @Schema(
        description = "Nome da pauta",
        example = "Pauta Importante",
        minLength = 3,
        maxLength = 100
    )
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, message = "Nome deve ter pelo menos 3 caracteres")
    private String name;

    @Schema(
        description = "Descrição detalhada da pauta",
        example = "Esta é uma descrição detalhada da pauta que será votada pelos associados",
        minLength = 10,
        maxLength = 500
    )
    @NotBlank(message = "Descrição é obrigatória")
    @Size(min = 10, message = "Descrição deve ter pelo menos 10 caracteres")
    private String description;

    @Schema(
        description = "Tempo de expiração da pauta em minutos",
        example = "10",
        minimum = "1"
    )
    @NotNull(message = "Expiração é obrigatória")
    @Positive(message = "Expiração deve ser maior que zero")
    private Integer expiration;
} 