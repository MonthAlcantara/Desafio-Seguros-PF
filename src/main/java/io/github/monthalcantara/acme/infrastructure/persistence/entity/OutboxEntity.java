package io.github.monthalcantara.acme.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OutboxEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "id_agregado", nullable = false)
    private String idAgregado;

    @Column(name = "tipo_agregado", nullable = false)
    private String tipoAgregado;

    @Column(name = "tipo_evento", nullable = false)
    private String tipoEvento;

    @Lob
    @Column(name = "conteudo", nullable = false, columnDefinition = "TEXT")
    private String conteudo;

    @Column(name = "data_criacao", nullable = false)
    private Instant dataCriacao;

    public OutboxEntity(UUID id, String idAgregado, String tipoAgregado, String tipoEvento, String conteudo) {
        this.id = id;
        this.idAgregado = idAgregado;
        this.tipoAgregado = tipoAgregado;
        this.tipoEvento = tipoEvento;
        this.conteudo = conteudo;
        this.dataCriacao = Instant.now();
    }
}
