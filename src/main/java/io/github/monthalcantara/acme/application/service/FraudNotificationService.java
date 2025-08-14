package io.github.monthalcantara.acme.application.service;

import io.github.monthalcantara.acme.domain.model.Solicitacao;
import java.util.concurrent.CompletableFuture;

public interface FraudNotificationService {
    CompletableFuture<Void> notifyAsync(final Solicitacao solicitacao);
}