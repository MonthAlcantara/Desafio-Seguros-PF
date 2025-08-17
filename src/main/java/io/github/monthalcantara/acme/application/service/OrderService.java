package io.github.monthalcantara.acme.application.service;

import io.github.monthalcantara.acme.domain.enums.TipoStatus;
import io.github.monthalcantara.acme.exception.SolicitacaoNaoEncontradaException;
import io.github.monthalcantara.acme.infra.kafka.event.OrderStatusEvent;
import io.github.monthalcantara.acme.infra.kafka.producer.OrderEventProducer;
import io.github.monthalcantara.acme.infra.persistence.entity.SolicitacaoEntity;
import io.github.monthalcantara.acme.infra.persistence.repository.SolicitacaoRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
public class OrderService {

//    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final SolicitacaoRepository repository;
    private final OrderEventProducer orderEventProducer;

    public OrderService(SolicitacaoRepository repository, OrderEventProducer orderEventProducer) {
        this.repository = repository;
        this.orderEventProducer = orderEventProducer;
    }

    @Transactional
    public void updateStatusFromPayment(UUID solicitacaoId, String status) {
        final var solicitacao = repository.findById(solicitacaoId).orElseThrow(() -> new SolicitacaoNaoEncontradaException(solicitacaoId));

        log.info("Atualizando status de pagamento para a Ordem ID: {}", solicitacaoId);

        final var tipoStatus = TipoStatus.fromDescricao(status);
        atualizar(solicitacao, tipoStatus, solicitacaoId);
    }

    @Transactional
    public void updateStatusFromSubscription(UUID solicitacaoId, String status) {
        final var solicitacao = repository.findById(solicitacaoId).orElseThrow(() -> new SolicitacaoNaoEncontradaException(solicitacaoId));
        log.info("Atualizando status de subscrição para a Ordem ID: {}", solicitacaoId);

        final var tipoStatus = TipoStatus.fromDescricao(status);
        atualizar(solicitacao, tipoStatus, solicitacaoId);
    }

    @Transactional
    private void atualizar(SolicitacaoEntity solicitacao, TipoStatus status, UUID solicitacaoId) {
        solicitacao.setAtualizacaoStatusNoHistorico(status);
        solicitacao.setFinalizadoEm(Instant.now());
        repository.save(solicitacao);
        orderEventProducer.send(new OrderStatusEvent(solicitacaoId, status.getDescricao(), Instant.now()));
    }
}
