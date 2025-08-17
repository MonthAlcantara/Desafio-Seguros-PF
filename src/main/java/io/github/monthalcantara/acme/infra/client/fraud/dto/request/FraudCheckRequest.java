package io.github.monthalcantara.acme.infra.client.fraud.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FraudCheckRequest {
    //TODO Renomear tudo internamente para portgestugues e usar @JsonProperty("customerId") ... @JsonProperty("orderId")
    @JsonProperty("customer_id")
    private UUID customerId;
    @JsonProperty("order_id")
    private UUID orderId;
}