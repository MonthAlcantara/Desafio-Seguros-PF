package io.github.monthalcantara.acme.domain.service;

import java.util.UUID;

public interface RemoveOutboxEventService {
    void remover(final UUID outboxId, final String classificacao);
}
