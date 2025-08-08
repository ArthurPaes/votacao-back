package com.example.pautachallenge.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Schema(
    name = "UserEntity",
    description = "Entidade de usuário do sistema",
    example = """
        {
            "id": 1,
            "name": "João Silva",
            "email": "joao.silva@example.com",
            "cpf": "12345678909",
            "password": "$2a$10$encryptedPasswordHash"
        }
        """
)
public class UserEntity {

    @Schema(description = "ID único do usuário", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Nome completo do usuário", example = "João Silva")
    @Column(nullable = false, length = 100)
    private String name;

    @Schema(description = "Endereço de email único", example = "joao.silva@example.com")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Schema(description = "CPF único do usuário", example = "12345678909")
    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    @Schema(description = "Senha criptografada", example = "$2a$10$encryptedPasswordHash")
    @Column(nullable = false)
    private String password;
}