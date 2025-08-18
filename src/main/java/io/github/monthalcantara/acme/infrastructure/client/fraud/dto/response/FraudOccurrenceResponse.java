package io.github.monthalcantara.acme.infrastructure.client.fraud.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class FraudOccurrenceResponse {
    private UUID id;

    @JsonProperty("productId")
    private Long productId;

    private String type;

    private String description;

    @JsonProperty("createdAt")
    private Instant createdAt;

    @JsonProperty("updatedAt")
    private Instant updatedAt;
}