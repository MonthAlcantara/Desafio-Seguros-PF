package io.github.monthalcantara.acme.infra.client.fraud.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
public class FraudCheckResponse {
    @JsonProperty("orderId")
    private UUID idSolicitacao;

    @JsonProperty("customerId")
    private UUID idCliente;

    @JsonProperty("analyzedAt")
    private Instant dataAnalise;

    @JsonProperty("classification")
    private String classificacao;

    @JsonProperty("occurrences")
    private List<FraudOccurrenceResponse> ocorrencias;
}