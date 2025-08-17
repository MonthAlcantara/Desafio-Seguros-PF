package io.github.monthalcantara.acme.infra.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FraudEvent {
    private UUID eventId;
    private UUID idSolicitacao;
    private UUID idCliente;
    private String classificacao;
    private Instant createdAt;
}