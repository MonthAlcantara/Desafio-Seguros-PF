package io.github.monthalcantara.acme.util.mapper.mapper;

import io.github.monthalcantara.acme.domain.model.Outbox;
import io.github.monthalcantara.acme.infrastructure.persistence.entity.OutboxEntity;

public class OutboxMapper {

    private OutboxMapper() {}


    public static OutboxEntity toEntity(Outbox outbox) {
        if (outbox == null) {
            return null;
        }
        return OutboxEntity.builder()
                .id(outbox.getId())
                .tipoAgregado(outbox.getTipoAgregado())
                .idAgregado(outbox.getIdAgregado())
                .tipoEvento(outbox.getTipoEvento())
                .conteudo(outbox.getConteudo())
                .dataCriacao(outbox.getDataCriacao())
                .build();
    }

    public static Outbox toModel(OutboxEntity outboxEntity) {
        if (outboxEntity == null) {
            return null;
        }
        return Outbox.builder()
                .id(outboxEntity.getId())
                .tipoAgregado(outboxEntity.getTipoAgregado())
                .idAgregado(outboxEntity.getIdAgregado())
                .tipoEvento(outboxEntity.getTipoEvento())
                .conteudo(outboxEntity.getConteudo())
                .dataCriacao(outboxEntity.getDataCriacao())
                .build();
    }
}
