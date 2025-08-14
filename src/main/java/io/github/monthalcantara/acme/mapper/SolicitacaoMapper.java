package io.github.monthalcantara.acme.mapper;

import io.github.monthalcantara.acme.api.dto.request.SolicitacaoRequest;
import io.github.monthalcantara.acme.api.dto.response.HistoricoMovimentacaoResponse;
import io.github.monthalcantara.acme.api.dto.response.SolicitacaoResponse;
import io.github.monthalcantara.acme.api.dto.response.SolicitacaoCriadaResponse;
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
import io.github.monthalcantara.acme.infra.persistence.entity.SolicitacaoEntity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class SolicitacaoMapper {

    public static Solicitacao toModel(final SolicitacaoRequest dto) {
        if (dto == null) return null;

        final var solicitacao = new Solicitacao();
        solicitacao.setClienteId(dto.getClienteId());
        solicitacao.setProdutoId(dto.getProdutoId());
        solicitacao.setCategoria(dto.getCategoria());
        solicitacao.setCanalVenda(dto.getCanalVenda());
        solicitacao.setMetodoPagamento(dto.getMetodoPagamento());
        solicitacao.setTotalPremioMensal(dto.getTotalPremioMensal());
        solicitacao.setValorSegurado(dto.getValorSegurado());
        solicitacao.setHistoricoMovimentacoes(List.of(HistoricoMovimentacao.builder()
                .dataMovimentacao(Instant.now())
                .status(TipoStatus.RECEBIDO.getDescricao())
                .build()));

        if (dto.getCoberturas() != null) {
            final var coberturas = dto.getCoberturas().entrySet().stream()
                    .map(e -> {
                        final var c = new Cobertura();
                        c.setNome(e.getKey());
                        c.setValor(e.getValue());
                        return c;
                    }).collect(Collectors.toList());
            solicitacao.setCoberturas(coberturas);
        }

        if (dto.getAssistencias() != null) {
            final var assistencias = dto.getAssistencias().stream()
                    .map(desc -> {
                        final var a = new Assistencia();
                        a.setDescricao(desc);
                        return a;
                    }).collect(Collectors.toList());
            solicitacao.setAssistencias(assistencias);
        }

        return solicitacao;
    }

    public static SolicitacaoResponse toDto(final Solicitacao model) {
        if (model == null) return null;

        final var response = new SolicitacaoResponse();
        response.setId(model.getId());
        response.setClienteId(model.getClienteId());
        response.setProdutoId(model.getProdutoId());
        response.setCategoria(model.getCategoria());
        response.setCanalVenda(model.getCanalVenda());
        response.setMetodoPagamento(model.getMetodoPagamento());
        response.setStatus(model.getStatus());
        response.setCriadoEm(model.getCriadoEm());
        response.setFinalizadoEm(model.getFinalizadoEm());
        response.setTotalPremioMensal(model.getTotalPremioMensal());
        response.setValorSegurado(model.getValorSegurado());
        model.getHistoricoMovimentacoes().forEach(h -> response.getHistoricoMovimentacoes().add(HistoricoMovimentacaoMapper.toResponse(h)));

        if (model.getCoberturas() != null) {
            final var mapCoberturas = model.getCoberturas().stream()
                    .collect(Collectors.toMap(Cobertura::getNome, Cobertura::getValor));
            response.setCoberturas(mapCoberturas);
        }

        if (model.getAssistencias() != null) {
            final var assistencias = model.getAssistencias().stream()
                    .map(Assistencia::getDescricao)
                    .collect(Collectors.toList());
            response.setAssistencias(assistencias);
        }

        return response;
    }

    public static SolicitacaoResponse toResponse(final Solicitacao model) {
        if (model == null) return null;

        return SolicitacaoResponse.builder()
                .id(model.getId())
                .clienteId(model.getClienteId())
                .produtoId(model.getProdutoId())
                .categoria(model.getCategoria())
                .canalVenda(model.getCanalVenda())
                .metodoPagamento(model.getMetodoPagamento())
                .status(model.getStatus())
                .criadoEm(model.getCriadoEm())
                .finalizadoEm(model.getFinalizadoEm())
                .totalPremioMensal(model.getTotalPremioMensal())
                .valorSegurado(model.getValorSegurado())
                .coberturas(mapCoberturas(model.getCoberturas()))
                .assistencias(mapAssistencias(model.getAssistencias()))
                .historicoMovimentacoes(mapHistorico(model.getHistoricoMovimentacoes()))
                .build();
    }

    private static Map<String, BigDecimal> mapCoberturas(final List<Cobertura> coberturas) {
        return coberturas == null ? Map.of() :
                coberturas.stream()
                        .collect(Collectors.toMap(Cobertura::getNome, Cobertura::getValor));
    }

    private static List<String> mapAssistencias(final List<Assistencia> assistencias) {
        return assistencias == null ? List.of() :
                assistencias.stream()
                        .map(Assistencia::getDescricao)
                        .toList();
    }

    private static List<HistoricoMovimentacaoResponse> mapHistorico(final List<HistoricoMovimentacao> historico) {
        return historico == null ? List.of() :
                historico.stream()
                        .map(h -> HistoricoMovimentacaoResponse.builder()
                                .status(h.getStatus())
                                .timestamp(h.getDataMovimentacao())
                                .build())
                        .toList();
    }

    public static List<SolicitacaoResponse> toResponseList(final List<Solicitacao> models) {
        if (models == null) return null;
        return models.stream()
                .map(SolicitacaoMapper::toResponse)
                .collect(Collectors.toList());
    }

    public static SolicitacaoCriadaResponse toResponseDto(final Solicitacao model) {
        if (model == null) return null;

        return new SolicitacaoCriadaResponse(
                model.getId(),
                model.getCriadoEm()
        );
    }

    public static SolicitacaoEntity toEntity(final Solicitacao model) {
        if (model == null) return null;

        final var entity = new SolicitacaoEntity();
        entity.setId(model.getId());
        entity.setClienteId(model.getClienteId());
        entity.setProdutoId(model.getProdutoId());
        entity.setCategoria(convertStringToCategoriaEnum(model.getCategoria()));
        entity.setCanalVenda(convertStringToCanalVendaEnum(model.getCanalVenda()));
        entity.setMetodoPagamento(convertStringToMetodoPagamentoEnum(model.getMetodoPagamento()));
        entity.setStatus(convertStringToStatusEnum(model.getStatus()));
        entity.setTotalPremioMensal(model.getTotalPremioMensal());
        entity.setValorSegurado(model.getValorSegurado());
        entity.setCriadoEm(model.getCriadoEm());
        entity.setFinalizadoEm(model.getFinalizadoEm());
        entity.setChaveIdempotencia(model.getChaveIdempotencia());
        entity.setHistoricoMovimentacoes(HistoricoMovimentacaoMapper.toEntityList(model.getHistoricoMovimentacoes(), entity));

        if (model.getCoberturas() != null) {
            final var coberturaEntities = model.getCoberturas().stream()
                    .map(cob -> {
                        final var entidade = new CoberturaEntity();
                        entidade.setNome(cob.getNome());
                        entidade.setValor(cob.getValor());
                        entidade.setSolicitacaoEntity(entity);
                        return entidade;
                    }).collect(Collectors.toList());
            entity.setCoberturaEntities(coberturaEntities);
        } else {
            entity.setCoberturaEntities(Collections.emptyList());
        }

        if (model.getAssistencias() != null) {
            final var assistenciaEntities = model.getAssistencias().stream()
                    .map(ass -> {
                        final var entidade = new AssistenciaEntity();
                        entidade.setDescricao(ass.getDescricao());
                        entidade.setSolicitacaoEntity(entity);
                        return entidade;
                    }).collect(Collectors.toList());
            entity.setAssistenciaEntities(assistenciaEntities);
        } else {
            entity.setAssistenciaEntities(Collections.emptyList());
        }

        return entity;
    }

    public static Solicitacao toModel(final SolicitacaoEntity entity) {
        if (entity == null) return null;

        final var model = new Solicitacao();
        model.setId(entity.getId());
        model.setClienteId(entity.getClienteId());
        model.setProdutoId(entity.getProdutoId());
        model.setCategoria(convertCategoriaEnumToString(entity.getCategoria()));
        model.setCanalVenda(convertCanalVendaEnumToString(entity.getCanalVenda()));
        model.setMetodoPagamento(convertMetodoPagamentoEnumToString(entity.getMetodoPagamento()));
        model.setStatus(convertStatusEnumToString(entity.getStatus()));
        model.setTotalPremioMensal(entity.getTotalPremioMensal());
        model.setValorSegurado(entity.getValorSegurado());
        model.setCriadoEm(entity.getCriadoEm());
        model.setFinalizadoEm(entity.getFinalizadoEm());
        model.setChaveIdempotencia(entity.getChaveIdempotencia());
        model.setHistoricoMovimentacoes(HistoricoMovimentacaoMapper.toDomainList(entity.getHistoricoMovimentacoes()));

        if (entity.getCoberturaEntities() != null) {
            final var coberturas = entity.getCoberturaEntities().stream()
                    .map(ent -> {
                        final var c = new Cobertura();
                        c.setNome(ent.getNome());
                        c.setValor(ent.getValor());
                        return c;
                    }).collect(Collectors.toList());
            model.setCoberturas(coberturas);
        }

        if (entity.getAssistenciaEntities() != null) {
            final var assistencias = entity.getAssistenciaEntities().stream()
                    .map(ent -> {
                        final var a = new Assistencia();
                        a.setDescricao(ent.getDescricao());
                        return a;
                    }).collect(Collectors.toList());
            model.setAssistencias(assistencias);
        }

        return model;
    }

    private static TipoCategoria convertStringToCategoriaEnum(final String categoria) {
        return TipoCategoria.fromDescricao(categoria);
    }

    private static TipoCanalVendas convertStringToCanalVendaEnum(final String canalVenda) {
        return TipoCanalVendas.fromDescricao(canalVenda);
    }

    private static TipoMetodoPagamento convertStringToMetodoPagamentoEnum(final String metodoPagamento) {
        return TipoMetodoPagamento.fromDescricao(metodoPagamento);
    }

    private static TipoStatus convertStringToStatusEnum(final String status) {
        return TipoStatus.fromDescricao(status);
    }

    private static String convertCategoriaEnumToString(final Object categoriaEnum) {
        return categoriaEnum == null ? null : categoriaEnum.toString();
    }

    private static String convertCanalVendaEnumToString(final Object canalVendaEnum) {
        return canalVendaEnum == null ? null : canalVendaEnum.toString();
    }

    private static String convertMetodoPagamentoEnumToString(final Object metodoPagamentoEnum) {
        return metodoPagamentoEnum == null ? null : metodoPagamentoEnum.toString();
    }

    private static String convertStatusEnumToString(final Object statusEnum) {
        return statusEnum == null ? null : statusEnum.toString();
    }
}