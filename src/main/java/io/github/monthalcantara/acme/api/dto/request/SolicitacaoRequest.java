package io.github.monthalcantara.acme.api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
public class SolicitacaoRequest {

    @JsonProperty("customerId")
    @NotNull(message = "{solicitacao.customerId.notnull}")
    private UUID clienteId;

    @JsonProperty("productId")
    @NotNull(message = "{solicitacao.productId.notnull}")
    private UUID produtoId;

    @JsonProperty("category")
    @NotNull(message = "{solicitacao.category.notnull}")
    private String categoria;

    @JsonProperty("salesChannel")
    @NotNull(message = "{solicitacao.salesChannel.notnull}")
    private String canalVenda;

    @JsonProperty("paymentMethod")
    @NotNull(message = "{solicitacao.paymentMethod.notnull}")
    private String metodoPagamento;

    @JsonProperty("totalMonthlyPremiumAmount")
    @NotNull(message = "{solicitacao.totalMonthlyPremiumAmount.notnull}")
    @Positive(message = "{solicitacao.totalMonthlyPremiumAmount.positive}")
    private BigDecimal totalPremioMensal;

    @JsonProperty("insuredAmount")
    @NotNull(message = "{solicitacao.insuredAmount.notnull}")
    @Positive(message = "{solicitacao.insuredAmount.positive}")
    private BigDecimal valorSegurado;

    @JsonProperty("coverages")
    @NotNull(message = "{solicitacao.coverages.notnull}")
    @Size(min = 1, message = "{solicitacao.coverages.size}")
    private Map<String, @Positive(message = "{solicitacao.coverages.value.positive}") BigDecimal> coberturas;

    @JsonProperty("assistances")
    @NotNull(message = "{solicitacao.assistances.notnull}")
    @Size(min = 1, message = "{solicitacao.assistances.size}")
    private List<String> assistencias;

}
