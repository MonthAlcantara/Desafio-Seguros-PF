package io.github.monthalcantara.acme.infra.fraud.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import java.time.Instant;
import java.util.UUID;

@Getter
public class FraudOccurrenceResponse {
    @JsonProperty("id")
    private UUID id;

    @JsonProperty("productId")
    private Integer idProduto;

    @JsonProperty("type")
    private String tipo;

    @JsonProperty("description")
    private String descricao;

    @JsonProperty("createdAt")
    private Instant dataCriacao;

    @JsonProperty("updatedAt")
    private Instant dataAtualizacao;
}