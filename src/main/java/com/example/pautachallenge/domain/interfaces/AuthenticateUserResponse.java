package com.example.pautachallenge.domain.interfaces;

import com.example.pautachallenge.domain.dto.UserResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "AuthenticateUserResponse",
    description = "Resposta de autenticação de usuário",
    example = """
        {
            "user": {
                "id": 1,
                "name": "João Silva",
                "email": "joao.silva@example.com",
                "cpf": "12345678909"
            },
            "errorMessage": null
        }
        """
)
public interface AuthenticateUserResponse {
    
    @Schema(description = "Dados do usuário autenticado")
    UserResponseDTO getUser();
    
    @Schema(description = "Mensagem de erro (se houver)", example = "Credenciais inválidas")
    String getErrorMessage();
}
