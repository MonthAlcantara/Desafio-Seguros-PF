package io.github.monthalcantara.acme.api.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO que representa um registro no histórico de movimentações de uma solicitação")
public class HistoricoMovimentacaoResponse {

    @Schema(description = "Status da solicitação no momento da movimentação", example = "RECEBIDO")
    private String status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    @Schema(description = "Timestamp da movimentação", example = "2024-05-15T10:30:00Z")
    private Instant timestamp;
}