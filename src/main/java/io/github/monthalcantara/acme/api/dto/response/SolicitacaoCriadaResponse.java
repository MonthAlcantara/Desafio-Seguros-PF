package io.github.monthalcantara.acme.api.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO de resposta para a criação de uma solicitação")
public class SolicitacaoCriadaResponse {

    @JsonProperty("id")
    @Schema(description = "ID único da solicitação criada", example = "920c57c4-54c3-4d43-85f0-f20387431102")
    private UUID id;

    @JsonProperty("createdAt")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    @Schema(description = "Timestamp da criação da solicitação", example = "2024-05-15T10:30:00Z")
    private Instant criadoEm;

}