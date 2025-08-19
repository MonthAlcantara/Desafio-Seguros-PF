package io.github.monthalcantara.acme.util.mapper;

import io.github.monthalcantara.acme.domain.model.Outbox;
import io.github.monthalcantara.acme.infrastructure.persistence.entity.OutboxEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OutboxMapperTest {

    @Test
    @DisplayName("deve converter Outbox para Entity com sucesso")
    void deveConverterOutboxParaEntity() {
        var outbox = Outbox.builder()
                .id(UUID.randomUUID())
                .tipoAgregado("Solicitacao")
                .idAgregado("123")
                .tipoEvento("CRIADO")
                .conteudo("{json}")
                .dataCriacao(Instant.now())
                .build();

        OutboxEntity entity = OutboxMapper.toEntity(outbox);

        assertNotNull(entity);
        assertEquals("Solicitacao", entity.getTipoAgregado());
    }

    @Test
    @DisplayName("deve converter Entity para Outbox com sucesso")
    void deveConverterEntityParaOutbox() {
        var entity = OutboxEntity.builder()
                .id(UUID.randomUUID())
                .tipoAgregado("Solicitacao")
                .idAgregado("123")
                .tipoEvento("CRIADO")
                .conteudo("{json}")
                .dataCriacao(Instant.now())
                .build();

        Outbox model = OutboxMapper.toModel(entity);

        assertNotNull(model);
        assertEquals("123", model.getIdAgregado());
    }

    @Test
    @DisplayName("deve retornar null se entrada for null")
    void deveRetornarNullSeEntradaForNull() {
        assertNull(OutboxMapper.toEntity(null));
        assertNull(OutboxMapper.toModel(null));
    }
}
