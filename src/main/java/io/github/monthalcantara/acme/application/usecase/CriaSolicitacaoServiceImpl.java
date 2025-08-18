package io.github.monthalcantara.acme.application.usecase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.monthalcantara.acme.domain.service.CriaSolicitacaoService;
import io.github.monthalcantara.acme.domain.model.Solicitacao;
import io.github.monthalcantara.acme.exception.ErroInternoException;
import io.github.monthalcantara.acme.infrastructure.kafka.event.OrderStatusEvent;
import io.github.monthalcantara.acme.infrastructure.kafka.producer.OrderEventProducer;
import io.github.monthalcantara.acme.infrastructure.persistence.entity.OutboxEntity;
import io.github.monthalcantara.acme.infrastructure.persistence.entity.SolicitacaoEntity;
import io.github.monthalcantara.acme.infrastructure.persistence.repository.OutboxRepository;
import io.github.monthalcantara.acme.infrastructure.persistence.repository.SolicitacaoRepository;
import io.github.monthalcantara.acme.util.mapper.mapper.SolicitacaoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
public class CriaSolicitacaoServiceImpl implements CriaSolicitacaoService {

    private final SolicitacaoRepository solicitacaoRepository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;
    private final OrderEventProducer orderEventProducer;

    public CriaSolicitacaoServiceImpl(final SolicitacaoRepository solicitacaoRepository, final OutboxRepository outboxRepository, final ObjectMapper objectMapper, OrderEventProducer orderEventProducer) {
        this.solicitacaoRepository = solicitacaoRepository;
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
        this.orderEventProducer = orderEventProducer;
    }

    @Transactional
    public Solicitacao criar(final Solicitacao solicitacao, final String chaveIdempotencia) {
        log.info("[Solicitacao] Início da criação. chaveIdempotencia={}", chaveIdempotencia);

        final var result = solicitacaoRepository.findByChaveIdempotencia(chaveIdempotencia)
                .map(SolicitacaoMapper::toModel)
                .orElseGet(() -> criarNova(solicitacao, chaveIdempotencia));

        log.info("[Solicitacao] Criação finalizada. id={}", result.getId());

        return result;
    }

    private Solicitacao criarNova(final Solicitacao solicitacao, final String chaveIdempotencia) {
        try {
            solicitacao.inicializarCamposDefault(chaveIdempotencia);

            final SolicitacaoEntity solicitacaoEntity = SolicitacaoMapper.toEntity(solicitacao);
            solicitacaoEntity.vincularRelacionamentos();
            final var solicitacaoSalva = solicitacaoRepository.save(solicitacaoEntity);
            log.info("[Solicitacao] Nova solicitação persistida. id={}, status={}", solicitacaoSalva.getId(), solicitacaoSalva.getStatus());

            final String solicitacaoJson = objectMapper.writeValueAsString(solicitacao);
            final OutboxEntity outboxEvent = new OutboxEntity(
                    solicitacaoSalva.getId(),
                    UUID.randomUUID().toString(),
                    "acme-solicitacao-criada",
                    "acme-solicitacao-criada",
                    solicitacaoJson
            );
            outboxRepository.save(outboxEvent);
            log.info("[Outbox] Evento criado na tabela outbox para a solicitação: {}", solicitacao.getId());
            orderEventProducer.send(new OrderStatusEvent(solicitacaoSalva.getId(), solicitacaoSalva.getStatus().getDescricao(), Instant.now()));

            return SolicitacaoMapper.toModel(solicitacaoSalva);
        } catch (JsonProcessingException e) {
            log.error("[Solicitacao] Erro ao serializar a solicitação para o outbox: {}", e.getMessage());
            throw new ErroInternoException("Falha ao serializar solicitação para evento outbox", e);
        }
    }
}
