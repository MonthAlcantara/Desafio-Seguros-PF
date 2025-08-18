package io.github.monthalcantara.acme.infrastructure.client.fraud.dto.request;

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
    @JsonProperty("customer_id")
    private UUID customerId;
    @JsonProperty("order_id")
    private UUID orderId;
}