package io.github.monthalcantara.acme.util.mapper;

import io.github.monthalcantara.acme.domain.enums.TipoStatus;
import io.github.monthalcantara.acme.domain.model.HistoricoMovimentacao;
import io.github.monthalcantara.acme.infrastructure.persistence.entity.HistoricoMovimentacaoEntity;
import io.github.monthalcantara.acme.infrastructure.persistence.entity.SolicitacaoEntity;
import io.github.monthalcantara.acme.infrastructure.web.dto.response.HistoricoMovimentacaoResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoricoMovimentacaoMapperTest {

    @Test
    @DisplayName("deve converter Entity para Domain")
    void deveConverterEntityParaDomain() {
        var entity = HistoricoMovimentacaoEntity.builder()
                .status(TipoStatus.RECEBIDO.getDescricao())
                .dataMovimentacao(Instant.now())
                .build();

        HistoricoMovimentacao domain = HistoricoMovimentacaoMapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(TipoStatus.RECEBIDO.getDescricao(), domain.getStatus());
    }

    @Test
    @DisplayName("deve converter Domain para Entity")
    void deveConverterDomainParaEntity() {
        var domain = HistoricoMovimentacao.builder()
                .status(TipoStatus.VALIDADO.getDescricao())
                .dataMovimentacao(Instant.now())
                .build();

        HistoricoMovimentacaoEntity entity = HistoricoMovimentacaoMapper.toEntity(domain, new SolicitacaoEntity());

        assertNotNull(entity);
        assertEquals(TipoStatus.VALIDADO.getDescricao(), entity.getStatus());
    }

    @Test
    @DisplayName("deve converter Domain para Response")
    void deveConverterDomainParaResponse() {
        var domain = HistoricoMovimentacao.builder()
                .status(TipoStatus.APROVADO.getDescricao())
                .dataMovimentacao(Instant.now())
                .build();

        HistoricoMovimentacaoResponse response = HistoricoMovimentacaoMapper.toResponse(domain);

        assertNotNull(response);
        assertEquals(TipoStatus.APROVADO.getDescricao(), response.getStatus());
    }

    @Test
    @DisplayName("deve converter Entity para Response")
    void deveConverterListaDeEntityParaListaDeDomain() {
        var entity = HistoricoMovimentacaoEntity.builder()
                .status(TipoStatus.PENDENTE.getDescricao())
                .dataMovimentacao(Instant.now())
                .build();

        List<HistoricoMovimentacao> lista = HistoricoMovimentacaoMapper.toDomainList(List.of(entity));

        assertEquals(1, lista.size());
    }

    @Test
    @DisplayName("deve converter Domain para Entity com SolicitacaoEntity")
    void deveConverterListaDeDomainParaListaDeEntity() {
        var domain = HistoricoMovimentacao.builder()
                .status(TipoStatus.PENDENTE.getDescricao())
                .dataMovimentacao(Instant.now())
                .build();

        List<HistoricoMovimentacaoEntity> lista = HistoricoMovimentacaoMapper.toEntityList(List.of(domain), new SolicitacaoEntity());

        assertEquals(1, lista.size());
    }

    @Test
    @DisplayName("deve converter Domain para Response")
    void deveConverterListaDeDomainParaListaDeResponse() {
        var domain = HistoricoMovimentacao.builder()
                .status(TipoStatus.PENDENTE.getDescricao())
                .dataMovimentacao(Instant.now())
                .build();

        List<HistoricoMovimentacaoResponse> lista = HistoricoMovimentacaoMapper.toResponseList(List.of(domain));

        assertEquals(1, lista.size());
        assertEquals(TipoStatus.PENDENTE.getDescricao(), lista.get(0).getStatus());
    }

    @Test
    @DisplayName("deve retornar coleção vazia quando entrada for null")
    void deveRetornarColecaoVaziaQuandoEntradaForNull() {
        assertTrue(HistoricoMovimentacaoMapper.toDomainList(null).isEmpty());
        assertTrue(HistoricoMovimentacaoMapper.toEntityList(null, null).isEmpty());
        assertTrue(HistoricoMovimentacaoMapper.toResponseList(null).isEmpty());
    }
}
