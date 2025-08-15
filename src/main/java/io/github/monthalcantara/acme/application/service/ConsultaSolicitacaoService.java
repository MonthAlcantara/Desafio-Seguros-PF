package io.github.monthalcantara.acme.application.service;

import io.github.monthalcantara.acme.domain.model.Solicitacao;
import io.github.monthalcantara.acme.exception.SolicitacaoNaoEncontradaException;
import io.github.monthalcantara.acme.infra.persistence.repository.SolicitacaoRepository;
import io.github.monthalcantara.acme.mapper.SolicitacaoMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ConsultaSolicitacaoService {

    private final SolicitacaoRepository repository;

    public ConsultaSolicitacaoService(final SolicitacaoRepository repository) {
        this.repository = repository;
    }

    public Solicitacao porId(final UUID id) {
        return repository.findById(id)
                .map(SolicitacaoMapper::toModel)
                .orElseThrow(() -> new SolicitacaoNaoEncontradaException(id));
    }

    public List<Solicitacao> porClienteId(final UUID clienteId) {
        return repository.findByClienteId(clienteId)
                .stream()
                .map(SolicitacaoMapper::toModel)
                .toList();
    }
}
