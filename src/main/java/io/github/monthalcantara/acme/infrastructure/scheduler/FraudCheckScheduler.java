package io.github.monthalcantara.acme.infrastructure.scheduler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.monthalcantara.acme.domain.service.AtualizaSolicitacaoService;
import io.github.monthalcantara.acme.domain.model.Solicitacao;
import io.github.monthalcantara.acme.infrastructure.client.fraud.FraudClient;
import io.github.monthalcantara.acme.infrastructure.persistence.entity.OutboxEntity;
import io.github.monthalcantara.acme.infrastructure.persistence.repository.OutboxRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
class FraudCheckScheduler {

    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;
    private final FraudClient fraudClient;
    private final AtualizaSolicitacaoService atualizaSolicitacaoService;

    public FraudCheckScheduler(
            final OutboxRepository outboxRepository,
            final ObjectMapper objectMapper,
            final FraudClient fraudClient, AtualizaSolicitacaoService atualizaSolicitacaoService) {
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
        this.fraudClient = fraudClient;
        this.atualizaSolicitacaoService = atualizaSolicitacaoService;
    }


    @Scheduled(fixedDelay = 20000)
    public void processOutboxForFraudCheck() {
        log.info("[FraudCheckScheduler] Iniciando checagem de fraudes do outbox...");

        final List<OutboxEntity> events = outboxRepository.findAll(PageRequest.of(0, 100)).getContent();

        if (events.isEmpty()) {
            log.info("[FraudCheckScheduler] Nenhum evento encontrado para checagem.");
            return;
        }

        log.info("[FraudCheckScheduler] {} eventos encontrados para processar.", events.size());

        for (final OutboxEntity event : events) {
            try {
                final Solicitacao solicitacao = objectMapper.readValue(event.getConteudo(), Solicitacao.class);

                final var fraudResponse = fraudClient.checkFraud(solicitacao);
                atualizaSolicitacaoService.atualizar(solicitacao.getId(), event, fraudResponse);

            } catch (final Exception e) {
                log.error("[FraudCheckScheduler] Falha ao processar evento {}. Erro: {}", event.getId(), e.getMessage());
            }
        }

        log.info("[FraudCheckScheduler] Checagem de fraudes concluída.");
    }
}
