package com.example.pautachallenge.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
    name = "UserDTO",
    description = "Dados para criação de um novo usuário",
    example = """
        {
            "name": "João Silva",
            "email": "joao.silva@example.com",
            "cpf": "12345678909",
            "password": "senha123"
        }
        """
)
public class UserDTO {

    @Schema(
        description = "Nome completo do usuário",
        example = "João Silva",
        minLength = 3,
        maxLength = 100
    )
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, message = "Nome deve ter pelo menos 3 caracteres")
    private String name;

    @Schema(
        description = "Endereço de email único do usuário",
        example = "joao.silva@example.com",
        format = "email"
    )
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser um endereço válido")
    private String email;

    @Schema(
        description = "CPF único do usuário (formato brasileiro)",
        example = "12345678909",
        pattern = "^\\d{11}$"
    )
    @NotBlank(message = "CPF é obrigatório")
    private String cpf;

    @Schema(
        description = "Senha do usuário (será criptografada)",
        example = "senha123",
        minLength = 6,
        maxLength = 100
    )
    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres")
    private String password;
} 