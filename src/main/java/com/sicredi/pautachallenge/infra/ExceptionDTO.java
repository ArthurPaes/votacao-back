package com.sicredi.pautachallenge.infra;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Schema(
    name = "ExceptionDTO",
    description = "Dados de resposta de erro",
    example = """
        {
            "message": "Data not found with provided ID",
            "status": 404
        }
        """
)
public class ExceptionDTO {
    
    @Schema(
        description = "Mensagem de erro",
        example = "Data not found with provided ID"
    )
    String message;
    
    @Schema(
        description = "Código de status HTTP",
        example = "404"
    )
    Integer status;
}
