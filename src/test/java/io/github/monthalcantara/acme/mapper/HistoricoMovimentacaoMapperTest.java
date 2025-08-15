package io.github.monthalcantara.acme.mapper;

import io.github.monthalcantara.acme.domain.model.HistoricoMovimentacao;
import io.github.monthalcantara.acme.infra.persistence.entity.HistoricoMovimentacaoEntity;
import io.github.monthalcantara.acme.infra.persistence.entity.SolicitacaoEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes de unidade para HistoricoMovimentacaoMapper")
class HistoricoMovimentacaoMapperTest {

    private final Instant timestamp = Instant.now();
    private final String status = "STATUS_TESTE";

    private HistoricoMovimentacaoEntity criarEntity() {
        return HistoricoMovimentacaoEntity.builder()
                .id(5L)
                .status(status)
                .dataMovimentacao(timestamp)
                .solicitacao(new SolicitacaoEntity())
                .build();
    }

    private HistoricoMovimentacao criarDomain() {
        return HistoricoMovimentacao.builder()
                .status(status)
                .dataMovimentacao(timestamp)
                .build();
    }

    @Test
    @DisplayName("Deve mapear HistoricoMovimentacaoEntity para Domain Model com sucesso")
    void deveMapearEntityParaDomain() {
        // Dado
        final var entity = criarEntity();

        // Quando
        final var domain = HistoricoMovimentacaoMapper.toDomain(entity);

        // Então
        assertNotNull(domain);
        assertEquals(entity.getStatus(), domain.getStatus());
        assertEquals(entity.getDataMovimentacao(), domain.getDataMovimentacao());
    }

    @Test
    @DisplayName("Deve retornar null ao mapear um HistoricoMovimentacaoEntity nulo para Domain Model")
    void deveRetornarNullAoMapearEntityNulaParaDomain() {
        // Quando
        final var domain = HistoricoMovimentacaoMapper.toDomain(null);

        // Então
        assertNull(domain);
    }

    @Test
    @DisplayName("Deve mapear Domain Model para HistoricoMovimentacaoEntity com sucesso")
    void deveMapearDomainParaEntity() {
        // Dado
        final var domain = criarDomain();
        final var solicitacaoEntity = new SolicitacaoEntity();

        // Quando
        final var entity = HistoricoMovimentacaoMapper.toEntity(domain, solicitacaoEntity);

        // Então
        assertNotNull(entity);
        assertEquals(domain.getStatus(), entity.getStatus());
        assertEquals(domain.getDataMovimentacao(), entity.getDataMovimentacao());
        assertEquals(solicitacaoEntity, entity.getSolicitacao());
    }

    @Test
    @DisplayName("Deve retornar null ao mapear um Domain Model nulo para HistoricoMovimentacaoEntity")
    void deveRetornarNullAoMapearDomainNuloParaEntity() {
        // Quando
        final var entity = HistoricoMovimentacaoMapper.toEntity(null, new SolicitacaoEntity());

        // Então
        assertNull(entity);
    }

    @Test
    @DisplayName("Deve mapear Domain Model para HistoricoMovimentacaoResponse com sucesso")
    void deveMapearDomainParaResponse() {
        // Dado
        final var domain = criarDomain();

        // Quando
        final var response = HistoricoMovimentacaoMapper.toResponse(domain);

        // Então
        assertNotNull(response);
        assertEquals(domain.getStatus(), response.getStatus());
        assertEquals(domain.getDataMovimentacao(), response.getTimestamp());
    }

    @Test
    @DisplayName("Deve retornar null ao mapear um Domain Model nulo para HistoricoMovimentacaoResponse")
    void deveRetornarNullAoMapearDomainNuloParaResponse() {
        // Quando
        final var response = HistoricoMovimentacaoMapper.toResponse(null);

        // Então
        assertNull(response);
    }

    @Test
    @DisplayName("Deve mapear uma lista de entities para uma lista de domains com sucesso")
    void deveMapearListaEntitiesParaDomains() {
        // Dado
        final var entityList = List.of(criarEntity());

        // Quando
        final var domainList = HistoricoMovimentacaoMapper.toDomainList(entityList);

        // Então
        assertNotNull(domainList);
        assertEquals(1, domainList.size());
        assertEquals(entityList.get(0).getStatus(), domainList.get(0).getStatus());
    }

    @Test
    @DisplayName("Deve retornar lista vazia ao mapear uma lista de entities nula para domains")
    void deveRetornarListaVaziaAoMapearListaEntityNula() {
        // Quando
        final var domainList = HistoricoMovimentacaoMapper.toDomainList(null);

        // Então
        assertNotNull(domainList);
        assertTrue(domainList.isEmpty());
    }

    @Test
    @DisplayName("Deve mapear uma lista de domains para uma lista de entities com sucesso")
    void deveMapearListaDomainsParaEntities() {
        // Dado
        final var domainList = List.of(criarDomain());
        final var solicitacaoEntity = new SolicitacaoEntity();

        // Quando
        final var entityList = HistoricoMovimentacaoMapper.toEntityList(domainList, solicitacaoEntity);

        // Então
        assertNotNull(entityList);
        assertEquals(1, entityList.size());
        assertEquals(domainList.get(0).getStatus(), entityList.get(0).getStatus());
        assertEquals(solicitacaoEntity, entityList.get(0).getSolicitacao());
    }

    @Test
    @DisplayName("Deve retornar lista vazia ao mapear uma lista de domains nula para entities")
    void deveRetornarListaVaziaAoMapearListaDomainNulaParaEntities() {
        // Quando
        final var entityList = HistoricoMovimentacaoMapper.toEntityList(null, new SolicitacaoEntity());

        // Então
        assertNotNull(entityList);
        assertTrue(entityList.isEmpty());
    }

    @Test
    @DisplayName("Deve mapear uma lista de domains para uma lista de responses com sucesso")
    void deveMapearListaDomainsParaResponses() {
        // Dado
        final var domainList = List.of(criarDomain());

        // Quando
        final var responseList = HistoricoMovimentacaoMapper.toResponseList(domainList);

        // Então
        assertNotNull(responseList);
        assertEquals(1, responseList.size());
        assertEquals(domainList.get(0).getStatus(), responseList.get(0).getStatus());
    }

    @Test
    @DisplayName("Deve retornar lista vazia ao mapear uma lista de domains nula para responses")
    void deveRetornarListaVaziaAoMapearListaDomainNulaParaResponses() {
        // Quando
        final var responseList = HistoricoMovimentacaoMapper.toResponseList(null);

        // Então
        assertNotNull(responseList);
        assertTrue(responseList.isEmpty());
    }
}