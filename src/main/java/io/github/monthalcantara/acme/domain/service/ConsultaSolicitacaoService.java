package io.github.monthalcantara.acme.domain.service;

import io.github.monthalcantara.acme.domain.model.Solicitacao;

import java.util.List;
import java.util.UUID;

public interface ConsultaSolicitacaoService {
    Solicitacao porId(final UUID id);

    List<Solicitacao> porClienteId(final UUID clienteId);
}
