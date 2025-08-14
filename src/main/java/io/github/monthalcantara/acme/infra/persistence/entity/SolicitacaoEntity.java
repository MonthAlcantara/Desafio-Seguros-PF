package io.github.monthalcantara.acme.infra.persistence.entity;

import io.github.monthalcantara.acme.domain.enums.TipoCanalVendas;
import io.github.monthalcantara.acme.domain.enums.TipoCategoria;
import io.github.monthalcantara.acme.domain.enums.TipoMetodoPagamento;
import io.github.monthalcantara.acme.domain.enums.TipoStatus;
import io.github.monthalcantara.acme.infra.persistence.converter.TipoCanalVendasConverter;
import io.github.monthalcantara.acme.infra.persistence.converter.TipoCategoriaConverter;
import io.github.monthalcantara.acme.infra.persistence.converter.TipoFormaPagamentoConverter;
import io.github.monthalcantara.acme.infra.persistence.converter.TipoStatusConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "solicitacao",
        indexes = {
                @Index(name = "idx_solicitacao_cliente_id", columnList = "cliente_id"),
                @Index(name = "idx_solicitacao_chave_idempotencia", columnList = "chave_idempotencia")
        }
)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SolicitacaoEntity {

    @Id
    private UUID id;

    @Column(name = "cliente_id", nullable = false)
    private UUID clienteId;

    @Column(name = "produto_id", nullable = false)
    private UUID produtoId;

    @Convert(converter = TipoCategoriaConverter.class)
    @Column(name = "categoria", nullable = false)
    private TipoCategoria categoria;

    @Convert(converter = TipoCanalVendasConverter.class)
    @Column(name = "canal_venda")
    private TipoCanalVendas canalVenda;

    @Convert(converter = TipoFormaPagamentoConverter.class)
    @Column(name = "metodo_pagamento")
    private TipoMetodoPagamento metodoPagamento;

    @Convert(converter = TipoStatusConverter.class)
    @Column(name = "status", nullable = false)
    private TipoStatus status;

    @Column(name = "premio_mensal_total", nullable = false)
    private BigDecimal totalPremioMensal;

    @Column(name = "valor_segurado", nullable = false)
    private BigDecimal valorSegurado;

    @Column(name = "criado_em", nullable = false)
    private Instant criadoEm;

    @Column(name = "finalizado_em")
    private Instant finalizadoEm;

    @Column(name = "chave_idempotencia", unique = true, length = 100)
    private String chaveIdempotencia;

    @Version
    @Column(name = "versao", nullable = false)
    private Long versao;

    @OneToMany(mappedBy = "solicitacaoEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CoberturaEntity> coberturaEntities;

    @OneToMany(mappedBy = "solicitacaoEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AssistenciaEntity> assistenciaEntities;

    @OneToMany(mappedBy = "solicitacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistoricoMovimentacaoEntity> historicoMovimentacoes = new ArrayList<>();

    public void addHistorico(String status, Instant when) {
        if (historicoMovimentacoes == null) {
            historicoMovimentacoes = new ArrayList<>();
        }
        HistoricoMovimentacaoEntity h = HistoricoMovimentacaoEntity.builder()
                .status(status)
                .dataMovimentacao(when)
                .solicitacao(this)
                .build();
        historicoMovimentacoes.add(h);
    }

    public void setStatusComHistorico(TipoStatus novoStatus) {
        this.status = novoStatus;
        addHistorico(novoStatus.getDescricao(), Instant.now());
    }

    public void vincularRelacionamentos() {
        getCoberturaEntities().forEach(cobertura -> cobertura.setSolicitacaoEntity(this));
        getAssistenciaEntities().forEach(assistencia -> assistencia.setSolicitacaoEntity(this));
        if (historicoMovimentacoes != null) {
            historicoMovimentacoes.forEach(h -> h.setSolicitacao(this));
        }
    }

    public List<HistoricoMovimentacaoEntity> getHistoricoMovimentacoes() {
        return historicoMovimentacoes == null ? Collections.emptyList() : historicoMovimentacoes;
    }

    public List<CoberturaEntity> getCoberturaEntities() {
        return coberturaEntities == null ? Collections.emptyList() : coberturaEntities;
    }

    public List<AssistenciaEntity> getAssistenciaEntities() {
        return assistenciaEntities == null ? Collections.emptyList() : assistenciaEntities;
    }
}
