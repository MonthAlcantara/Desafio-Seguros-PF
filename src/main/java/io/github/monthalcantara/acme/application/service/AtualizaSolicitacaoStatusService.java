package io.github.monthalcantara.acme.application.service;

import io.github.monthalcantara.acme.domain.enums.TipoStatus;
import io.github.monthalcantara.acme.exception.SolicitacaoNaoEncontradaException;
import io.github.monthalcantara.acme.exception.StatusNaoPermitidoException;
import io.github.monthalcantara.acme.infra.client.fraud.dto.response.FraudCheckResponse;
import io.github.monthalcantara.acme.infra.persistence.entity.OutboxEntity;
import io.github.monthalcantara.acme.infra.persistence.entity.SolicitacaoEntity;
import io.github.monthalcantara.acme.infra.persistence.repository.SolicitacaoRepository;
import io.github.monthalcantara.acme.mapper.SolicitacaoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AtualizaSolicitacaoStatusService {

    private final SolicitacaoRepository solicitacaoRepository;
    private final ValidadorDeRegrasAdicionaisService validadorDeRegrasAdicionaisService;
    private final RemoveOutboxEventService removeOutboxEventService;

    @Transactional
    public void atualizar(final UUID solicitacaoId, final OutboxEntity event, final FraudCheckResponse response) {
        log.info("[Status] Iniciando atualização de status. ID={}, Classificação de Fraude: {}", solicitacaoId, response.getClassificacao());

        solicitacaoRepository.findById(solicitacaoId).ifPresent(solicitacao -> {
            final TipoStatus novoStatus = validadorDeRegrasAdicionaisService.validar(SolicitacaoMapper.toModel(solicitacao), response.getClassificacao());

            solicitacao.setAtualizacaoStatusNoHistorico(novoStatus);
            solicitacaoRepository.save(solicitacao);
            removeOutboxEventService.remover(solicitacaoId, novoStatus.getDescricao());
            log.info("[Status] Finalizada a atualização. Solicitação ID: {}, Novo Status: {}", solicitacaoId, novoStatus.getDescricao());
        });
    }

    @Transactional
    public void cancelar(final UUID solicitacaoId) {
        SolicitacaoEntity solicitacao = solicitacaoRepository.findById(solicitacaoId)
                .orElseThrow(() -> new SolicitacaoNaoEncontradaException("Solicitação não encontrada: " + solicitacaoId));

        final var status = solicitacao.getStatus();
        if (status == TipoStatus.APROVADO || status == TipoStatus.REJEITADO) {
            throw new StatusNaoPermitidoException("Não é possível cancelar uma solicitação com status " + status.getDescricao());
        }

        solicitacao.setAtualizacaoStatusNoHistorico(TipoStatus.CANCELADA);
        solicitacao.setFinalizadoEm(Instant.now());
        solicitacaoRepository.save(solicitacao);
        removeOutboxEventService.remover(solicitacaoId, status.getDescricao());

        // TODO: Publicar evento de cancelamento para outros serviços
    }
}