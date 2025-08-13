package io.github.monthalcantara.acme.api.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SolicitacaoResponse {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("customer_id")
    private UUID clienteId;

    @JsonProperty("product_id")
    private UUID produtoId;

    @JsonProperty("category")
    private String categoria;

    @JsonProperty("salesChannel")
    private String canalVenda;

    @JsonProperty("paymentMethod")
    private String metodoPagamento;

    @JsonProperty("status")
    private String status;

    @JsonProperty("createdAt")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant criadoEm;

    @JsonProperty("finishedAt")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant finalizadoEm;

    @JsonProperty("total_monthly_premium_amount")
    private BigDecimal totalPremioMensal;

    @JsonProperty("insured_amount")
    private BigDecimal valorSegurado;

    @JsonProperty("coverages")
    private Map<String, BigDecimal> coberturas;

    @JsonProperty("assistances")
    private List<String> assistencias;

    @JsonProperty("history")
    private List<HistoricoMovimentacaoResponse> historicoMovimentacoes;

}
