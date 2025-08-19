package io.github.monthalcantara.acme.application.usecase;

import io.github.monthalcantara.acme.domain.service.AtualizaSolicitacaoService;
import io.github.monthalcantara.acme.domain.service.RemoveOutboxEventService;
import io.github.monthalcantara.acme.domain.service.ValidadorDeRegrasAdicionaisService;
import io.github.monthalcantara.acme.domain.enums.TipoStatus;
import io.github.monthalcantara.acme.exception.SolicitacaoNaoEncontradaException;
import io.github.monthalcantara.acme.exception.StatusNaoPermitidoException;
import io.github.monthalcantara.acme.infrastructure.client.fraud.dto.response.FraudCheckResponse;
import io.github.monthalcantara.acme.infrastructure.kafka.event.OrderStatusEvent;
import io.github.monthalcantara.acme.infrastructure.kafka.producer.OrderEventProducer;
import io.github.monthalcantara.acme.infrastructure.persistence.entity.OutboxEntity;
import io.github.monthalcantara.acme.infrastructure.persistence.repository.SolicitacaoRepository;
import io.github.monthalcantara.acme.util.mapper.SolicitacaoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
public class AtualizaSolicitacaoServiceImpl implements AtualizaSolicitacaoService {

    private final SolicitacaoRepository solicitacaoRepository;
    private final ValidadorDeRegrasAdicionaisService validadorDeRegrasAdicionaisService;
    private final RemoveOutboxEventService removeOutboxEventService;

    private final OrderEventProducer orderEventProducer;

    public AtualizaSolicitacaoServiceImpl(SolicitacaoRepository solicitacaoRepository, ValidadorDeRegrasAdicionaisService validadorDeRegrasAdicionaisService, RemoveOutboxEventService removeOutboxEventService, OrderEventProducer orderEventProducer) {
        this.solicitacaoRepository = solicitacaoRepository;
        this.validadorDeRegrasAdicionaisService = validadorDeRegrasAdicionaisService;
        this.removeOutboxEventService = removeOutboxEventService;
        this.orderEventProducer = orderEventProducer;
    }

    @Transactional
    public void atualizar(final UUID solicitacaoId, final OutboxEntity event, final FraudCheckResponse response) {
        log.info("[Status] Iniciando atualização de status. ID={}, Classificação de Fraude: {}", solicitacaoId, response.getClassificacao());

        solicitacaoRepository.findById(solicitacaoId).ifPresent(solicitacao -> {
            final TipoStatus novoStatus = validadorDeRegrasAdicionaisService.validar(SolicitacaoMapper.toModel(solicitacao), response.getClassificacao());
            solicitacao.setAtualizacaoStatusNoHistorico(novoStatus);
            log.info("[Status] Atualizando solicitação ID: {}. Status atual: {}, Novo Status: {}", solicitacaoId, solicitacao.getStatus().getDescricao(), novoStatus.getDescricao());
            final var salvo = solicitacaoRepository.save(solicitacao);
            removeOutboxEventService.remover(solicitacaoId, novoStatus.getDescricao());
            log.info("[Status] Finalizada a atualização. Solicitação ID: {}, Novo Status: {}", solicitacaoId, novoStatus.getDescricao());
            orderEventProducer.send(new OrderStatusEvent(solicitacaoId, salvo.getStatus().getDescricao(), Instant.now()));

        });
    }

    @Transactional
    public void cancelar(final UUID solicitacaoId) {
        final var solicitacao = solicitacaoRepository.findById(solicitacaoId)
                .orElseThrow(() -> new SolicitacaoNaoEncontradaException("Solicitação não encontrada: " + solicitacaoId));

        final var status = solicitacao.getStatus();
        if (TipoStatus.isStatusFinal(solicitacao.getStatus().getDescricao())) {
            throw new StatusNaoPermitidoException("Não é possível cancelar uma solicitação com status " + status.getDescricao());
        }

        solicitacao.setAtualizacaoStatusNoHistorico(TipoStatus.CANCELADA);
        solicitacao.setFinalizadoEm(Instant.now());
        log.info("[Status] Cancelando solicitação ID: {}. Status atual: {}", solicitacaoId, status.getDescricao());
        final var salvo = solicitacaoRepository.save(solicitacao);
        removeOutboxEventService.remover(solicitacaoId, status.getDescricao());
        log.info("[Status] Solicitação ID: {} cancelada com sucesso. Novo Status: {}", solicitacaoId, TipoStatus.CANCELADA.getDescricao());
        orderEventProducer.send(new OrderStatusEvent(solicitacaoId, salvo.getStatus().getDescricao(), Instant.now()));

    }
}