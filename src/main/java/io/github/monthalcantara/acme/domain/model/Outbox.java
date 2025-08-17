package io.github.monthalcantara.acme.domain.model;

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
public class Outbox {

    private UUID id;

    private String idAgregado;

    private String tipoAgregado;

    private String tipoEvento;

    private String conteudo;

    private Instant dataCriacao;
}