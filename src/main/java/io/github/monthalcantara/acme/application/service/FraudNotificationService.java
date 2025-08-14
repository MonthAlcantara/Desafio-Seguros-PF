package io.github.monthalcantara.acme.application.service;

import io.github.monthalcantara.acme.domain.model.Solicitacao;

public interface FraudNotificationService {
    void notifyAsync(final Solicitacao solicitacao);
}