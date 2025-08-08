package com.example.pautachallenge.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "votes")
@Schema(
    name = "Votes",
    description = "Entidade de voto em uma pauta",
    example = """
        {
            "id": 1,
            "sectionId": 1,
            "userId": 1,
            "vote": true,
            "status": "ABLE_TO_VOTE"
        }
        """
)
public class Votes {

    @Schema(description = "ID único do voto", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "ID da pauta onde o voto foi registrado", example = "1")
    @Column(name = "section_id", nullable = false)
    private Long sectionId;

    @Schema(description = "ID do usuário que votou", example = "1")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Schema(description = "Voto do usuário (true = a favor, false = contra)", example = "true")
    @Column(nullable = false)
    private Boolean vote;

    @Schema(description = "Status do voto (ABLE_TO_VOTE, UNABLE_TO_VOTE)", example = "ABLE_TO_VOTE")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VoteStatus status;
}