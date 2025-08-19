package io.github.monthalcantara.acme.mapper;

import io.github.monthalcantara.acme.domain.model.HistoricoMovimentacao;
import io.github.monthalcantara.acme.infrastructure.persistence.entity.HistoricoMovimentacaoEntity;
import io.github.monthalcantara.acme.infrastructure.persistence.entity.SolicitacaoEntity;
import io.github.monthalcantara.acme.util.mapper.HistoricoMovimentacaoMapper;
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

        final var entity = criarEntity();


        final var domain = HistoricoMovimentacaoMapper.toDomain(entity);


        assertNotNull(domain);
        assertEquals(entity.getStatus(), domain.getStatus());
        assertEquals(entity.getDataMovimentacao(), domain.getDataMovimentacao());
    }

    @Test
    @DisplayName("Deve retornar null ao mapear um HistoricoMovimentacaoEntity nulo para Domain Model")
    void deveRetornarNullAoMapearEntityNulaParaDomain() {

        final var domain = HistoricoMovimentacaoMapper.toDomain(null);


        assertNull(domain);
    }

    @Test
    @DisplayName("Deve mapear Domain Model para HistoricoMovimentacaoEntity com sucesso")
    void deveMapearDomainParaEntity() {

        final var domain = criarDomain();
        final var solicitacaoEntity = new SolicitacaoEntity();


        final var entity = HistoricoMovimentacaoMapper.toEntity(domain, solicitacaoEntity);


        assertNotNull(entity);
        assertEquals(domain.getStatus(), entity.getStatus());
        assertEquals(domain.getDataMovimentacao(), entity.getDataMovimentacao());
        assertEquals(solicitacaoEntity, entity.getSolicitacao());
    }

    @Test
    @DisplayName("Deve retornar null ao mapear um Domain Model nulo para HistoricoMovimentacaoEntity")
    void deveRetornarNullAoMapearDomainNuloParaEntity() {

        final var entity = HistoricoMovimentacaoMapper.toEntity(null, new SolicitacaoEntity());


        assertNull(entity);
    }

    @Test
    @DisplayName("Deve mapear Domain Model para HistoricoMovimentacaoResponse com sucesso")
    void deveMapearDomainParaResponse() {

        final var domain = criarDomain();


        final var response = HistoricoMovimentacaoMapper.toResponse(domain);


        assertNotNull(response);
        assertEquals(domain.getStatus(), response.getStatus());
        assertEquals(domain.getDataMovimentacao(), response.getTimestamp());
    }

    @Test
    @DisplayName("Deve retornar null ao mapear um Domain Model nulo para HistoricoMovimentacaoResponse")
    void deveRetornarNullAoMapearDomainNuloParaResponse() {

        final var response = HistoricoMovimentacaoMapper.toResponse(null);


        assertNull(response);
    }

    @Test
    @DisplayName("Deve mapear uma lista de entities para uma lista de domains com sucesso")
    void deveMapearListaEntitiesParaDomains() {

        final var entityList = List.of(criarEntity());


        final var domainList = HistoricoMovimentacaoMapper.toDomainList(entityList);


        assertNotNull(domainList);
        assertEquals(1, domainList.size());
        assertEquals(entityList.get(0).getStatus(), domainList.get(0).getStatus());
    }

    @Test
    @DisplayName("Deve retornar lista vazia ao mapear uma lista de entities nula para domains")
    void deveRetornarListaVaziaAoMapearListaEntityNula() {

        final var domainList = HistoricoMovimentacaoMapper.toDomainList(null);


        assertNotNull(domainList);
        assertTrue(domainList.isEmpty());
    }

    @Test
    @DisplayName("Deve mapear uma lista de domains para uma lista de entities com sucesso")
    void deveMapearListaDomainsParaEntities() {

        final var domainList = List.of(criarDomain());
        final var solicitacaoEntity = new SolicitacaoEntity();


        final var entityList = HistoricoMovimentacaoMapper.toEntityList(domainList, solicitacaoEntity);


        assertNotNull(entityList);
        assertEquals(1, entityList.size());
        assertEquals(domainList.get(0).getStatus(), entityList.get(0).getStatus());
        assertEquals(solicitacaoEntity, entityList.get(0).getSolicitacao());
    }

    @Test
    @DisplayName("Deve retornar lista vazia ao mapear uma lista de domains nula para entities")
    void deveRetornarListaVaziaAoMapearListaDomainNulaParaEntities() {

        final var entityList = HistoricoMovimentacaoMapper.toEntityList(null, new SolicitacaoEntity());


        assertNotNull(entityList);
        assertTrue(entityList.isEmpty());
    }

    @Test
    @DisplayName("Deve mapear uma lista de domains para uma lista de responses com sucesso")
    void deveMapearListaDomainsParaResponses() {

        final var domainList = List.of(criarDomain());


        final var responseList = HistoricoMovimentacaoMapper.toResponseList(domainList);


        assertNotNull(responseList);
        assertEquals(1, responseList.size());
        assertEquals(domainList.get(0).getStatus(), responseList.get(0).getStatus());
    }

    @Test
    @DisplayName("Deve retornar lista vazia ao mapear uma lista de domains nula para responses")
    void deveRetornarListaVaziaAoMapearListaDomainNulaParaResponses() {

        final var responseList = HistoricoMovimentacaoMapper.toResponseList(null);


        assertNotNull(responseList);
        assertTrue(responseList.isEmpty());
    }
}