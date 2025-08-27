package com.sicredi.pautachallenge.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sections")
@Schema(
    name = "Section",
    description = "Entidade de pauta para votação",
    example = """
        {
            "id": 1,
            "name": "Pauta Importante",
            "description": "Esta é uma descrição detalhada da pauta que será votada pelos associados",
            "expiration": 10,
            "start_at": "2024-01-15T10:00:00"
        }
        """
)
public class Section {

    @Schema(description = "ID único da pauta", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Nome da pauta", example = "Pauta Importante")
    @Column(nullable = false, length = 100)
    private String name;

    @Schema(description = "Descrição detalhada da pauta", example = "Esta é uma descrição detalhada da pauta que será votada pelos associados")
    @Column(nullable = false, length = 500)
    private String description;

    @Schema(description = "Tempo de expiração em minutos", example = "10")
    @Column(nullable = false)
    private Integer expiration;

    @Schema(description = "Data e hora de início da pauta", example = "2024-01-15T10:00:00")
    @Column(name = "start_at", nullable = false)
    private LocalDateTime start_at;
}
