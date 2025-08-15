package io.github.monthalcantara.acme.mapper;

import io.github.monthalcantara.acme.api.dto.request.SolicitacaoRequest;
import io.github.monthalcantara.acme.domain.enums.TipoCanalVendas;
import io.github.monthalcantara.acme.domain.enums.TipoCategoria;
import io.github.monthalcantara.acme.domain.enums.TipoMetodoPagamento;
import io.github.monthalcantara.acme.domain.enums.TipoStatus;
import io.github.monthalcantara.acme.domain.model.Assistencia;
import io.github.monthalcantara.acme.domain.model.Cobertura;
import io.github.monthalcantara.acme.domain.model.HistoricoMovimentacao;
import io.github.monthalcantara.acme.domain.model.Solicitacao;
import io.github.monthalcantara.acme.infra.persistence.entity.AssistenciaEntity;
import io.github.monthalcantara.acme.infra.persistence.entity.CoberturaEntity;
import io.github.monthalcantara.acme.infra.persistence.entity.HistoricoMovimentacaoEntity;
import io.github.monthalcantara.acme.infra.persistence.entity.SolicitacaoEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes de unidade para SolicitacaoMapper")
class SolicitacaoMapperTest {

    private SolicitacaoRequest criarSolicitacaoRequest() {
        return new SolicitacaoRequest(
                UUID.randomUUID(),
                UUID.randomUUID(),
                TipoCategoria.AUTO.getDescricao(),
                TipoCanalVendas.MOBILE.getDescricao(),
                TipoMetodoPagamento.CARTAO_CREDITO.getDescricao(),
                BigDecimal.valueOf(150.75),
                BigDecimal.valueOf(50000.00),
                Map.of("Cobertura Basica", BigDecimal.valueOf(25000.00)),
                List.of("Assistência 24h")
        );
    }

    private Solicitacao criarSolicitacaoModel() {
        final var model = new Solicitacao();
        model.setId(UUID.randomUUID());
        model.setClienteId(UUID.randomUUID());
        model.setProdutoId(UUID.randomUUID());
        model.setCategoria(TipoCategoria.AUTO.getDescricao());
        model.setCanalVenda(TipoCanalVendas.MOBILE.getDescricao());
        model.setMetodoPagamento(TipoMetodoPagamento.CARTAO_CREDITO.getDescricao());
        model.setStatus(TipoStatus.APROVADO.getDescricao());
        model.setCriadoEm(Instant.now());
        model.setFinalizadoEm(Instant.now());
        model.setTotalPremioMensal(BigDecimal.valueOf(150.75));
        model.setValorSegurado(BigDecimal.valueOf(50000.00));
        model.setChaveIdempotencia(UUID.randomUUID().toString());

        final var cobertura = new Cobertura();
        cobertura.setNome("Cobertura Basica");
        cobertura.setValor(BigDecimal.valueOf(25000.00));
        model.setCoberturas(List.of(cobertura));

        final var assistencia = new Assistencia();
        assistencia.setDescricao("Assistência 24h");
        model.setAssistencias(List.of(assistencia));

        final var historico = HistoricoMovimentacao.builder()
                .status(TipoStatus.APROVADO.getDescricao())
                .dataMovimentacao(Instant.now())
                .build();
        model.setHistoricoMovimentacoes(List.of(historico));

        return model;
    }

    private SolicitacaoEntity criarSolicitacaoEntity() {
        final var entity = new SolicitacaoEntity();
        entity.setId(UUID.randomUUID());
        entity.setClienteId(UUID.randomUUID());
        entity.setProdutoId(UUID.randomUUID());
        entity.setCategoria(TipoCategoria.AUTO);
        entity.setCanalVenda(TipoCanalVendas.MOBILE);
        entity.setMetodoPagamento(TipoMetodoPagamento.CARTAO_CREDITO);
        entity.setStatus(TipoStatus.APROVADO);
        entity.setCriadoEm(Instant.now());
        entity.setFinalizadoEm(Instant.now());
        entity.setTotalPremioMensal(BigDecimal.valueOf(150.75));
        entity.setValorSegurado(BigDecimal.valueOf(50000.00));
        entity.setChaveIdempotencia(UUID.randomUUID().toString());

        final var coberturaEntity = new CoberturaEntity();
        coberturaEntity.setNome("Cobertura Basica");
        coberturaEntity.setValor(BigDecimal.valueOf(25000.00));
        coberturaEntity.setSolicitacaoEntity(entity);
        entity.setCoberturaEntities(List.of(coberturaEntity));

        final var assistenciaEntity = new AssistenciaEntity();
        assistenciaEntity.setDescricao("Assistência 24h");
        assistenciaEntity.setSolicitacaoEntity(entity);
        entity.setAssistenciaEntities(List.of(assistenciaEntity));

        final var historicoEntity = HistoricoMovimentacaoEntity.builder()
                .status(TipoStatus.APROVADO.getDescricao())
                .dataMovimentacao(Instant.now())
                .solicitacao(entity)
                .build();
        entity.setHistoricoMovimentacoes(List.of(historicoEntity));

        return entity;
    }

    @Test
    @DisplayName("Deve mapear SolicitacaoRequest para Domain Model com sucesso")
    void deveMapearRequestParaModel() {
        // Dado
        final var request = criarSolicitacaoRequest();

        // Quando
        final var model = SolicitacaoMapper.toModel(request);

        // Então
        assertNotNull(model);
        assertEquals(request.getClienteId(), model.getClienteId());
        assertEquals(request.getProdutoId(), model.getProdutoId());
        assertEquals(request.getCategoria(), model.getCategoria());
        assertEquals(request.getCanalVenda(), model.getCanalVenda());
        assertEquals(request.getMetodoPagamento(), model.getMetodoPagamento());
        assertEquals(request.getTotalPremioMensal(), model.getTotalPremioMensal());
        assertEquals(request.getValorSegurado(), model.getValorSegurado());
        assertFalse(model.getCoberturas().isEmpty());
        assertFalse(model.getAssistencias().isEmpty());
        assertEquals(TipoStatus.RECEBIDO.getDescricao(), model.getHistoricoMovimentacoes().get(0).getStatus());
    }

    @Test
    @DisplayName("Deve retornar null ao mapear um SolicitacaoRequest nulo para Domain Model")
    void deveRetornarNullAoMapearRequestNulo() {
        // Quando
        final var model = SolicitacaoMapper.toModel((SolicitacaoEntity) null);

        // Então
        assertNull(model);
    }

    @Test
    @DisplayName("Deve mapear Domain Model para SolicitacaoResponse com sucesso (usando builder)")
    void deveMapearModelParaResponseComBuilder() {
        // Dado
        final var model = criarSolicitacaoModel();

        // Quando
        final var response = SolicitacaoMapper.toResponse(model);

        // Então
        assertNotNull(response);
        assertEquals(model.getId(), response.getId());
        assertEquals(model.getClienteId(), response.getClienteId());
        assertEquals(model.getStatus(), response.getStatus());
        assertEquals(model.getCoberturas().get(0).getNome(), response.getCoberturas().keySet().stream().findFirst().get());
        assertEquals(model.getAssistencias().get(0).getDescricao(), response.getAssistencias().get(0));
        assertEquals(model.getHistoricoMovimentacoes().get(0).getStatus(), response.getHistoricoMovimentacoes().get(0).getStatus());
    }

    @Test
    @DisplayName("Deve retornar null ao mapear um Domain Model nulo para SolicitacaoResponse")
    void deveRetornarNullAoMapearModelNuloParaResponse() {
        // Quando
        final var response = SolicitacaoMapper.toResponse(null);

        // Então
        assertNull(response);
    }

    @Test
    @DisplayName("Deve mapear Domain Model para SolicitacaoCriadaResponse com sucesso")
    void deveMapearModelParaSolicitacaoCriadaResponse() {
        // Dado
        final var model = criarSolicitacaoModel();

        // Quando
        final var response = SolicitacaoMapper.toResponseDto(model);

        // Então
        assertNotNull(response);
        assertEquals(model.getId(), response.getId());
        assertEquals(model.getCriadoEm(), response.getCriadoEm());
    }

    @Test
    @DisplayName("Deve retornar null ao mapear um Domain Model nulo para SolicitacaoCriadaResponse")
    void deveRetornarNullAoMapearModelNuloParaCriadaResponse() {
        // Quando
        final var response = SolicitacaoMapper.toResponseDto(null);

        // Então
        assertNull(response);
    }

    @Test
    @DisplayName("Deve mapear Domain Model para SolicitacaoEntity com sucesso")
    void deveMapearModelParaEntity() {
        // Dado
        final var model = criarSolicitacaoModel();

        // Quando
        final var entity = SolicitacaoMapper.toEntity(model);

        // Então
        assertNotNull(entity);
        assertEquals(model.getId(), entity.getId());
        assertEquals(model.getClienteId(), entity.getClienteId());
        assertEquals(TipoCategoria.AUTO, entity.getCategoria());
        assertEquals(TipoCanalVendas.MOBILE, entity.getCanalVenda());
        assertEquals(TipoMetodoPagamento.CARTAO_CREDITO, entity.getMetodoPagamento());
        assertEquals(TipoStatus.APROVADO, entity.getStatus());
        assertEquals(model.getCoberturas().get(0).getNome(), entity.getCoberturaEntities().get(0).getNome());
        assertEquals(model.getAssistencias().get(0).getDescricao(), entity.getAssistenciaEntities().get(0).getDescricao());
        assertFalse(entity.getHistoricoMovimentacoes().isEmpty());
    }

    @Test
    @DisplayName("Deve retornar null ao mapear um Domain Model nulo para SolicitacaoEntity")
    void deveRetornarNullAoMapearModelNuloParaEntity() {
        // Quando
        final var entity = SolicitacaoMapper.toEntity(null);

        // Então
        assertNull(entity);
    }

    @Test
    @DisplayName("Deve mapear SolicitacaoEntity para Domain Model com sucesso")
    void deveMapearEntityParaModel() {
        // Dado
        final var entity = criarSolicitacaoEntity();

        // Quando
        final var model = SolicitacaoMapper.toModel(entity);

        // Então
        assertNotNull(model);
        assertEquals(entity.getId(), model.getId());
        assertEquals(entity.getClienteId(), model.getClienteId());
        assertEquals(entity.getCategoria().getDescricao(), model.getCategoria());
        assertEquals(entity.getCanalVenda().getDescricao(), model.getCanalVenda());
        assertEquals(entity.getMetodoPagamento().getDescricao(), model.getMetodoPagamento());
        assertEquals(entity.getStatus().getDescricao(), model.getStatus());
        assertEquals(entity.getCoberturaEntities().get(0).getNome(), model.getCoberturas().get(0).getNome());
        assertEquals(entity.getAssistenciaEntities().get(0).getDescricao(), model.getAssistencias().get(0).getDescricao());
        assertFalse(model.getHistoricoMovimentacoes().isEmpty());
    }

    @Test
    @DisplayName("Deve retornar null ao mapear uma SolicitacaoEntity nula para Domain Model")
    void deveRetornarNullAoMapearEntityNulaParaModel() {
        // Quando
        final var model = SolicitacaoMapper.toModel((SolicitacaoRequest) null);

        // Então
        assertNull(model);
    }
}