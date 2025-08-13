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
    // Converte SolicitacaoRequest → Solicitacao (domain)
    public static Solicitacao toModel(SolicitacaoRequest dto) {
        if (dto == null) return null;

        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setClienteId(dto.getClienteId());
        solicitacao.setProdutoId(dto.getProdutoId());
        solicitacao.setCategoria(dto.getCategoria());
        solicitacao.setCanalVenda(dto.getCanalVenda());
        solicitacao.setMetodoPagamento(dto.getMetodoPagamento());
        solicitacao.setTotalPremioMensal(dto.getTotalPremioMensal());
        solicitacao.setValorSegurado(dto.getValorSegurado());
        solicitacao.setHistoricoMovimentacoes(List.of(HistoricoMovimentacao.builder().dataMovimentacao(Instant.now()).status(TipoStatus.RECEBIDO.getDescricao()).build()));

        // Map<String, BigDecimal> → List<Cobertura>
        if (dto.getCoberturas() != null) {
            List<Cobertura> coberturas = dto.getCoberturas().entrySet().stream()
                    .map(e -> {
                        Cobertura c = new Cobertura();
                        c.setNome(e.getKey());
                        c.setValor(e.getValue());
                        return c;
                    }).collect(Collectors.toList());
            solicitacao.setCoberturas(coberturas);
        }

        // List<String> → List<Assistencia>
        if (dto.getAssistencias() != null) {
            List<Assistencia> assistencias = dto.getAssistencias().stream()
                    .map(desc -> {
                        Assistencia a = new Assistencia();
                        a.setDescricao(desc);
                        return a;
                    }).collect(Collectors.toList());
            solicitacao.setAssistencias(assistencias);
        }

        return solicitacao;
    }

    // Converte Solicitacao (domain) → SolicitacaoResponse (DTO)
    public static SolicitacaoResponse toDto(Solicitacao model) {
        if (model == null) return null;

        SolicitacaoResponse response = new SolicitacaoResponse();
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

        // List<Cobertura> → Map<String, BigDecimal>
        if (model.getCoberturas() != null) {
            Map<String, BigDecimal> mapCoberturas = model.getCoberturas().stream()
                    .collect(Collectors.toMap(Cobertura::getNome, Cobertura::getValor));
            response.setCoberturas(mapCoberturas);
        }

        // List<Assistencia> → List<String>
        if (model.getAssistencias() != null) {
            List<String> assistencias = model.getAssistencias().stream()
                    .map(Assistencia::getDescricao)
                    .collect(Collectors.toList());
            response.setAssistencias(assistencias);
        }

        return response;
    }

    public static SolicitacaoResponse toResponse(Solicitacao model) {
        if (model == null) {
            return null;
        }

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

    private static Map<String, BigDecimal> mapCoberturas(List<Cobertura> coberturas) {
        return coberturas == null ? Map.of() :
                coberturas.stream()
                        .collect(Collectors.toMap(Cobertura::getNome, Cobertura::getValor));
    }

    private static List<String> mapAssistencias(List<Assistencia> assistencias) {
        return assistencias == null ? List.of() :
                assistencias.stream()
                        .map(Assistencia::getDescricao)
                        .toList();
    }

    private static List<HistoricoMovimentacaoResponse> mapHistorico(List<HistoricoMovimentacao> historico) {
        return historico == null ? List.of() :
                historico.stream()
                        .map(h -> HistoricoMovimentacaoResponse.builder()
                                .status(h.getStatus())
                                .timestamp(h.getDataMovimentacao())
                                .build())
                        .toList();
    }

    public static List<SolicitacaoResponse> toResponseList(List<Solicitacao> models) {
        if (models == null) {
            return null;
        }
        return models.stream()
                .map(SolicitacaoMapper::toResponse)
                .collect(Collectors.toList());
    }

    // Converte Solicitacao (domain) → SolicitacaoResponse (DTO)
    public static SolicitacaoCriadaResponse toResponseDto(Solicitacao model) {
        if (model == null) return null;

        return new SolicitacaoCriadaResponse(
                model.getId(),
                model.getCriadoEm()
        );
    }

    // Converte Solicitacao (domain) → SolicitacaoEntity
    public static SolicitacaoEntity toEntity(Solicitacao model) {
        if (model == null) return null;

        SolicitacaoEntity entity = new SolicitacaoEntity();
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


        // Map Coberturas domain → entity e seta solicitacaoEntity no relacionamento
        if (model.getCoberturas() != null) {
            List<CoberturaEntity> coberturaEntities = model.getCoberturas().stream()
                    .map(cob -> {
                        CoberturaEntity entidade = new CoberturaEntity();
                        entidade.setNome(cob.getNome());
                        entidade.setValor(cob.getValor());
                        entidade.setSolicitacaoEntity(entity);
                        return entidade;
                    }).collect(Collectors.toList());
            entity.setCoberturaEntities(coberturaEntities);
        } else {
            entity.setCoberturaEntities(Collections.emptyList());
        }

        // Map Assistencias domain → entity e seta solicitacaoEntity no relacionamento
        if (model.getAssistencias() != null) {
            List<AssistenciaEntity> assistenciaEntities = model.getAssistencias().stream()
                    .map(ass -> {
                        AssistenciaEntity entidade = new AssistenciaEntity();
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

    // Converte SolicitacaoEntity → Solicitacao (domain)
    public static Solicitacao toModel(SolicitacaoEntity entity) {
        if (entity == null) return null;

        Solicitacao model = new Solicitacao();
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


        // List<CoberturaEntity> → List<Cobertura>
        if (entity.getCoberturaEntities() != null) {
            List<Cobertura> coberturas = entity.getCoberturaEntities().stream()
                    .map(ent -> {
                        Cobertura c = new Cobertura();
                        c.setNome(ent.getNome());
                        c.setValor(ent.getValor());
                        return c;
                    }).collect(Collectors.toList());
            model.setCoberturas(coberturas);
        }

        // List<AssistenciaEntity> → List<Assistencia>
        if (entity.getAssistenciaEntities() != null) {
            List<Assistencia> assistencias = entity.getAssistenciaEntities().stream()
                    .map(ent -> {
                        Assistencia a = new Assistencia();
                        a.setDescricao(ent.getDescricao());
                        return a;
                    }).collect(Collectors.toList());
            model.setAssistencias(assistencias);
        }

        return model;
    }

    // Métodos para converter entre String e seus enums na entity (implemente conforme seus enums)
    private static TipoCategoria convertStringToCategoriaEnum(String categoria) {
        return TipoCategoria.fromDescricao(categoria); // modificar para seu enum
    }

    private static TipoCanalVendas convertStringToCanalVendaEnum(String canalVenda) {
        return TipoCanalVendas.fromDescricao(canalVenda); // modificar para seu enum
    }

    private static TipoMetodoPagamento convertStringToMetodoPagamentoEnum(String metodoPagamento) {
        return TipoMetodoPagamento.fromDescricao(metodoPagamento); // modificar para seu enum
    }

    private static TipoStatus convertStringToStatusEnum(String status) {
        return TipoStatus.fromDescricao(status); // modificar para seu enum
    }

    private static String convertCategoriaEnumToString(Object categoriaEnum) {
        return categoriaEnum == null ? null : categoriaEnum.toString();
    }

    private static String convertCanalVendaEnumToString(Object canalVendaEnum) {
        return canalVendaEnum == null ? null : canalVendaEnum.toString();
    }

    private static String convertMetodoPagamentoEnumToString(Object metodoPagamentoEnum) {
        return metodoPagamentoEnum == null ? null : metodoPagamentoEnum.toString();
    }

    private static String convertStatusEnumToString(Object statusEnum) {
        return statusEnum == null ? null : statusEnum.toString();
    }
}