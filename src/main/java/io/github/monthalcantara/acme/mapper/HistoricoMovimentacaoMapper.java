package io.github.monthalcantara.acme.mapper;

import io.github.monthalcantara.acme.api.dto.response.HistoricoMovimentacaoResponse;
import io.github.monthalcantara.acme.domain.model.HistoricoMovimentacao;
import io.github.monthalcantara.acme.infra.persistence.entity.HistoricoMovimentacaoEntity;
import io.github.monthalcantara.acme.infra.persistence.entity.SolicitacaoEntity;
import java.util.Collections;

import java.util.List;
import java.util.stream.Collectors;

public class HistoricoMovimentacaoMapper {

    // Entity → Domain
    public static HistoricoMovimentacao toDomain(HistoricoMovimentacaoEntity entity) {
        if (entity == null) return null;

        return HistoricoMovimentacao.builder()
                .status(entity.getStatus())
                .dataMovimentacao(entity.getDataMovimentacao())
                .build();
    }

    // Domain → Entity
    public static HistoricoMovimentacaoEntity toEntity(HistoricoMovimentacao domain, SolicitacaoEntity solicitacaoEntity) {
        if (domain == null) return null;

        return HistoricoMovimentacaoEntity.builder()
                .status(domain.getStatus())
                .dataMovimentacao(domain.getDataMovimentacao())
                .solicitacao(solicitacaoEntity) // obrigatório setar o relacionamento
                .build();
    }

    // Domain → DTO Response
    public static HistoricoMovimentacaoResponse toResponse(HistoricoMovimentacao domain) {
        if (domain == null) return null;

        return HistoricoMovimentacaoResponse.builder()
                .status(domain.getStatus())
                .timestamp(domain.getDataMovimentacao())
                .build();
    }

    // List<Entity> → List<Domain>
    public static List<HistoricoMovimentacao> toDomainList(List<HistoricoMovimentacaoEntity> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream()
                .map(HistoricoMovimentacaoMapper::toDomain)
                .collect(Collectors.toList());
    }

    // List<Domain> → List<DTO>
    public static List<HistoricoMovimentacaoEntity> toEntityList(List<HistoricoMovimentacao> domains, SolicitacaoEntity solicitacaoEntity) {
        if (domains == null) return Collections.emptyList();
        return domains.stream()
                .map(h -> HistoricoMovimentacaoMapper.toEntity(h, solicitacaoEntity))
                .collect(Collectors.toList());
    }
    // List<Domain> → List<DTO>
    public static List<HistoricoMovimentacaoResponse> toResponseList(List<HistoricoMovimentacao> domains) {
        if (domains == null) return Collections.emptyList();
        return domains.stream()
                .map(HistoricoMovimentacaoMapper::toResponse)
                .collect(Collectors.toList());
    }
}
