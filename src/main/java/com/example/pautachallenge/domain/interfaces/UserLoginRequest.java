package com.example.pautachallenge.domain.interfaces;

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
    name = "UserLoginRequest",
    description = "Dados para autenticação de usuário",
    example = """
        {
            "email": "joao.silva@example.com",
            "password": "senha123"
        }
        """
)
public class UserLoginRequest {

    @Schema(
        description = "Endereço de email do usuário",
        example = "joao.silva@example.com",
        format = "email"
    )
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser um endereço válido")
    private String email;

    @Schema(
        description = "Senha do usuário",
        example = "senha123",
        minLength = 6
    )
    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres")
    private String password;
}
