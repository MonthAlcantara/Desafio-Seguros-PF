package io.github.monthalcantara.acme.application.service;

import io.github.monthalcantara.acme.domain.model.Solicitacao;
import io.github.monthalcantara.acme.infra.persistence.entity.SolicitacaoEntity;
import io.github.monthalcantara.acme.infra.persistence.repository.SolicitacaoRepository;
import io.github.monthalcantara.acme.mapper.SolicitacaoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CriaSolicitacaoService {

    private final SolicitacaoRepository repository;
    private final FraudNotificationService fraudNotificationService;

    @Transactional
    public Solicitacao criar(final Solicitacao solicitacao, final String chaveIdempotencia) {
        log.info("[Solicitacao] Início da criação. chaveIdempotencia={}", chaveIdempotencia);
        Solicitacao result = repository.findByChaveIdempotencia(chaveIdempotencia)
                .map(SolicitacaoMapper::toModel)
                .orElseGet(() -> criarNova(solicitacao, chaveIdempotencia));

        log.info("[Solicitacao] Criação finalizada. id={}", result.getId());

        if (result.getStatus().equalsIgnoreCase("recebido")) {
            log.info("[Fraude] Acionando notificação assíncrona para ID: {}", result.getId());
            fraudNotificationService.notifyAsync(result);
        } else {
            log.warn("[Fraude] Não acionado para ID: {}. Status da solicitação não é 'RECEBIDO'. Status: {}", result.getId(), result.getStatus());
        }

        return result;
    }

    private Solicitacao criarNova(final Solicitacao solicitacao, final String chaveIdempotencia) {
        solicitacao.inicializarCamposDefault(chaveIdempotencia);

        final SolicitacaoEntity entidade = SolicitacaoMapper.toEntity(solicitacao);
        entidade.vincularRelacionamentos();

        final var solicitacaoSalva = repository.save(entidade);
        log.info("[Solicitacao] Nova solicitação persistida. id={}, status={}", solicitacaoSalva.getId(), solicitacaoSalva.getStatus());

        return SolicitacaoMapper.toModel(solicitacaoSalva);
    }
}