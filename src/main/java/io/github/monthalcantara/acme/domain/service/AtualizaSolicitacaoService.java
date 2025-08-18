package io.github.monthalcantara.acme.domain.service;

import io.github.monthalcantara.acme.infrastructure.client.fraud.dto.response.FraudCheckResponse;
import io.github.monthalcantara.acme.infrastructure.persistence.entity.OutboxEntity;

import java.util.UUID;


public interface AtualizaSolicitacaoService {
    void atualizar(final UUID solicitacaoId, final OutboxEntity event, final FraudCheckResponse response);
    void cancelar(final UUID solicitacaoId);
}