package io.github.monthalcantara.acme.infra.fraud.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class FraudCheckRequest {
    @JsonProperty("customerId")
    private UUID idCliente;

    @JsonProperty("proposalId")
    private UUID idSolicitacao;
}