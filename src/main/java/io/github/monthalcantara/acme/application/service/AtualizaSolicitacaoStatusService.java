package io.github.monthalcantara.acme.application.service;

import io.github.monthalcantara.acme.domain.enums.TipoStatus;
import io.github.monthalcantara.acme.infra.fraud.dto.response.FraudCheckResponse;
import io.github.monthalcantara.acme.infra.persistence.repository.SolicitacaoRepository;
import io.github.monthalcantara.acme.mapper.SolicitacaoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AtualizaSolicitacaoStatusService {

    private final SolicitacaoRepository solicitacaoRepository;
    private final ValidadorDeRegrasAdicionaisService validadorDeRegrasAdicionaisService;

    @Transactional
    public void atualizarStatusComBaseEmFraude(final UUID solicitacaoId, final FraudCheckResponse response) {
        log.info("[Status] Iniciando atualização de status. ID={}, Classificação de Fraude: {}", solicitacaoId, response.getClassificacao());

        solicitacaoRepository.findById(solicitacaoId).ifPresent(solicitacao -> {
            final TipoStatus novoStatus = validadorDeRegrasAdicionaisService.validar(SolicitacaoMapper.toModel(solicitacao), response.getClassificacao());

            solicitacao.setStatusComHistorico(novoStatus);
            solicitacaoRepository.save(solicitacao);

            log.info("[Status] Finalizada a atualização. Solicitação ID: {}, Novo Status: {}", solicitacaoId, novoStatus.getDescricao());
        });
    }
}