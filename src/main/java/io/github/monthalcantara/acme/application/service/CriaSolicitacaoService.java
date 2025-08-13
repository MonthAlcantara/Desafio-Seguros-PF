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

    @Transactional
    public Solicitacao criar(final Solicitacao solicitacao, final String chaveIdempotencia) {
        log.info("[Solicitacao] Criação solicitada. clienteId={}, produtoId={}", solicitacao.getClienteId(), solicitacao.getProdutoId());
        Solicitacao result = repository.findByChaveIdempotencia(chaveIdempotencia)
                .map(SolicitacaoMapper::toModel)
                .orElseGet(() -> criarNova(solicitacao, chaveIdempotencia));

        log.info("[Solicitacao] Criação concluída. id={}", result.getId());
        return result;
    }

    private Solicitacao criarNova(final Solicitacao solicitacao, final String chaveIdempotencia) {
        // Inicializa valores
        solicitacao.inicializarCamposDefault(chaveIdempotencia);

        // Mapeia model -> entity
        final SolicitacaoEntity entidade = SolicitacaoMapper.toEntity(solicitacao);
        entidade.vincularRelacionamentos();

        // Persiste (status inicial RECEBIDO)
        final var solicitacaoSalva = repository.save(entidade);
        log.info("[Solicitacao] Nova solicitação criada. id={}, clienteId={}, status={}", solicitacaoSalva.getId(), solicitacaoSalva.getClienteId(), solicitacaoSalva.getStatus());

        // Retorna model
        return SolicitacaoMapper.toModel(solicitacaoSalva);
    }
}
