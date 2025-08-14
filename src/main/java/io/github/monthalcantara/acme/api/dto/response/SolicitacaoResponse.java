package io.github.monthalcantara.acme.api.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO de resposta detalhada de uma solicitação de apólice")
public class SolicitacaoResponse {

    @JsonProperty("id")
    @Schema(description = "ID único da solicitação", example = "920c57c4-54c3-4d43-85f0-f20387431102")
    private UUID id;

    @JsonProperty("customer_id")
    @Schema(description = "ID do cliente", example = "920c57c4-54c3-4d43-85f0-f20387431102")
    private UUID clienteId;

    @JsonProperty("product_id")
    @Schema(description = "ID do produto de seguro", example = "a202d847-a87d-4a1d-a08b-12d8325a772c")
    private UUID produtoId;

    @JsonProperty("category")
    @Schema(description = "Categoria da apólice", example = "CARRO")
    private String categoria;

    @JsonProperty("salesChannel")
    @Schema(description = "Canal de venda", example = "APP")
    private String canalVenda;

    @JsonProperty("paymentMethod")
    @Schema(description = "Método de pagamento", example = "CREDIT_CARD")
    private String metodoPagamento;

    @JsonProperty("status")
    @Schema(description = "Status atual da solicitação", example = "RECEBIDO")
    private String status;

    @JsonProperty("createdAt")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    @Schema(description = "Timestamp da criação da solicitação", example = "2024-05-15T10:30:00Z")
    private Instant criadoEm;

    @JsonProperty("finishedAt")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    @Schema(description = "Timestamp da finalização da solicitação", example = "2024-05-15T10:35:00Z")
    private Instant finalizadoEm;

    @JsonProperty("total_monthly_premium_amount")
    @Schema(description = "Valor total do prêmio mensal", example = "150.75")
    private BigDecimal totalPremioMensal;

    @JsonProperty("insured_amount")
    @Schema(description = "Valor total segurado", example = "50000.00")
    private BigDecimal valorSegurado;

    @JsonProperty("coverages")
    @Schema(description = "Mapa de coberturas contratadas e seus valores segurados")
    private Map<String, BigDecimal> coberturas;

    @JsonProperty("assistances")
    @Schema(description = "Lista de assistências contratadas")
    private List<String> assistencias;

    @JsonProperty("history")
    @Schema(description = "Histórico de movimentações da solicitação")
    private List<HistoricoMovimentacaoResponse> historicoMovimentacoes;

}