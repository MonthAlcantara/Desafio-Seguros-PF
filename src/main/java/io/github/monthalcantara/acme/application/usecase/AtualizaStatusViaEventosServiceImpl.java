package io.github.monthalcantara.acme.application.usecase;

import io.github.monthalcantara.acme.domain.service.AtualizaStatusViaEventosService;
import io.github.monthalcantara.acme.domain.enums.TipoStatus;
import io.github.monthalcantara.acme.exception.SolicitacaoNaoEncontradaException;
import io.github.monthalcantara.acme.infrastructure.kafka.event.OrderStatusEvent;
import io.github.monthalcantara.acme.infrastructure.kafka.producer.OrderEventProducer;
import io.github.monthalcantara.acme.infrastructure.persistence.entity.SolicitacaoEntity;
import io.github.monthalcantara.acme.infrastructure.persistence.repository.SolicitacaoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
public class AtualizaStatusViaEventosServiceImpl implements AtualizaStatusViaEventosService {

    private final SolicitacaoRepository repository;
    private final OrderEventProducer orderEventProducer;

    public AtualizaStatusViaEventosServiceImpl(SolicitacaoRepository repository, OrderEventProducer orderEventProducer) {
        this.repository = repository;
        this.orderEventProducer = orderEventProducer;
    }

    @Transactional
    public void atualizaStatusMediantePagamento(UUID solicitacaoId, String status) {
        final var solicitacao = repository.findById(solicitacaoId).orElseThrow(() -> new SolicitacaoNaoEncontradaException(solicitacaoId));

        log.info("[OrderService] Atualizando status de pagamento para a Ordem ID: {}", solicitacaoId);

        final var tipoStatus = TipoStatus.fromDescricao(status);
        atualizar(solicitacao, tipoStatus, solicitacaoId);
    }

    @Transactional
    public void atualizaStatusMedianteSubscricao(UUID solicitacaoId, String status) {
        final var solicitacao = repository.findById(solicitacaoId).orElseThrow(() -> new SolicitacaoNaoEncontradaException(solicitacaoId));
        log.info("[OrderService] Atualizando status de subscrição para a Ordem ID: {}", solicitacaoId);

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
