package io.github.monthalcantara.acme.domain.model;

import io.github.monthalcantara.acme.domain.enums.TipoStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Solicitacao {
    private UUID id;
    private UUID clienteId;
    private UUID produtoId;
    private String categoria;
    private String canalVenda;
    private String metodoPagamento;
    private String status;
    private BigDecimal totalPremioMensal;
    private BigDecimal valorSegurado;
    private Instant criadoEm;
    private Instant finalizadoEm;
    private String chaveIdempotencia;
    private List<Cobertura> coberturas;
    private List<Assistencia> assistencias;
    private List<HistoricoMovimentacao> historicoMovimentacoes;

    public void inicializarCamposDefault(String chaveIdempotencia) {
        this.id = UUID.randomUUID();
        this.status = TipoStatus.RECEBIDO.getDescricao();
        this.criadoEm = Instant.now();
        this.chaveIdempotencia = chaveIdempotencia;
    }
}
