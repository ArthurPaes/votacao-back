package com.example.pautachallenge.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
    name = "UserResponseDTO",
    description = "Dados de resposta de um usuário (sem senha)",
    example = """
        {
            "id": 1,
            "name": "João Silva",
            "email": "joao.silva@example.com",
            "cpf": "12345678909"
        }
        """
)
public class UserResponseDTO {

    @Schema(
        description = "ID único do usuário",
        example = "1"
    )
    private Long id;

    @Schema(
        description = "Nome completo do usuário",
        example = "João Silva"
    )
    private String name;

    @Schema(
        description = "Endereço de email do usuário",
        example = "joao.silva@example.com"
    )
    private String email;

    @Schema(
        description = "CPF do usuário",
        example = "12345678909"
    )
    private String cpf;
} 