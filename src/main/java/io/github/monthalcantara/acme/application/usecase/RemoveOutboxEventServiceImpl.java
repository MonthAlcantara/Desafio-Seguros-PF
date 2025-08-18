package io.github.monthalcantara.acme.application.usecase;

import io.github.monthalcantara.acme.domain.service.RemoveOutboxEventService;
import io.github.monthalcantara.acme.domain.enums.TipoStatus;
import io.github.monthalcantara.acme.infrastructure.persistence.repository.OutboxRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class RemoveOutboxEventServiceImpl implements RemoveOutboxEventService {

    private final OutboxRepository outboxRepository;

    public RemoveOutboxEventServiceImpl(final OutboxRepository outboxRepository) {
        this.outboxRepository = outboxRepository;
    }

    public void remover(final UUID outboxId, final String classificacao) {

        final var event = outboxRepository.findById(outboxId);
        if (event.isEmpty()) {
            log.warn("[TransactionUpdateService] Tentativa de remover evento de outbox não encontrado: {}", outboxId);
        } else {

            log.info("[TransactionUpdateService] Iniciando atualização transacional para o evento ID: {}", outboxId);

            if (TipoStatus.APROVADO.getDescricao().equalsIgnoreCase(classificacao) || TipoStatus.VALIDADO.getDescricao().equalsIgnoreCase(classificacao)) {
                log.info("[TransactionUpdateService] Transação associada ao evento {} {}.", outboxId, classificacao);
            } else if (TipoStatus.REJEITADO.getDescricao().equalsIgnoreCase(classificacao) || TipoStatus.CANCELADA.getDescricao().equalsIgnoreCase(classificacao)) {
                log.info("[TransactionUpdateService] Transação associada ao evento {} marcada como fraude.", outboxId);
            } else {
                log.warn("[TransactionUpdateService] Classificação desconhecida: {}. Ação de atualização não definida.", classificacao);
            }

            outboxRepository.delete(event.get());

            log.info("[TransactionUpdateService] Evento {} removido do outbox. Transação concluída.", outboxId);
        }
    }
}
