package io.github.monthalcantara.acme.api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Schema(description = "DTO para a criação de uma nova solicitação de apólice de seguro")
public class SolicitacaoRequest {

    @JsonProperty("customerId")
    @NotNull(message = "{solicitacao.customerId.notnull}")
    @Schema(description = "ID do cliente", example = "920c57c4-54c3-4d43-85f0-f20387431102")
    private UUID clienteId;

    @JsonProperty("productId")
    @NotNull(message = "{solicitacao.productId.notnull}")
    @Schema(description = "ID do produto de seguro", example = "a202d847-a87d-4a1d-a08b-12d8325a772c")
    private UUID produtoId;

    @JsonProperty("category")
    @NotNull(message = "{solicitacao.category.notnull}")
    @Schema(description = "Categoria da apólice", example = "AUTO")
    private String categoria;

    @JsonProperty("salesChannel")
    @NotNull(message = "{solicitacao.salesChannel.notnull}")
    @Schema(description = "Canal de venda", example = "MOBILE")
    private String canalVenda;

    @JsonProperty("paymentMethod")
    @NotNull(message = "{solicitacao.paymentMethod.notnull}")
    @Schema(description = "Método de pagamento", example = "CREDIT_CARD")
    private String metodoPagamento;

    @JsonProperty("totalMonthlyPremiumAmount")
    @NotNull(message = "{solicitacao.totalMonthlyPremiumAmount.notnull}")
    @Positive(message = "{solicitacao.totalMonthlyPremiumAmount.positive}")
    @Schema(description = "Valor total do prêmio mensal", example = "150.75")
    private BigDecimal totalPremioMensal;

    @JsonProperty("insuredAmount")
    @NotNull(message = "{solicitacao.insuredAmount.notnull}")
    @Positive(message = "{solicitacao.insuredAmount.positive}")
    @Schema(description = "Valor total segurado", example = "50000.00")
    private BigDecimal valorSegurado;

    @JsonProperty("coverages")
    @NotNull(message = "{solicitacao.coverages.notnull}")
    @Size(min = 1, message = "{solicitacao.coverages.size}")
    @Schema(description = "Mapa de coberturas contratadas e seus valores segurados")
    private Map<String, @Positive(message = "{solicitacao.coverages.value.positive}") BigDecimal> coberturas;

    @JsonProperty("assistances")
    @NotNull(message = "{solicitacao.assistances.notnull}")
    @Size(min = 1, message = "{solicitacao.assistances.size}")
    @Schema(description = "Lista de assistências contratadas")
    private List<String> assistencias;

}