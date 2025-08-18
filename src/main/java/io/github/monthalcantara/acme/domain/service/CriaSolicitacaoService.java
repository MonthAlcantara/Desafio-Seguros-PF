package io.github.monthalcantara.acme.domain.service;

import io.github.monthalcantara.acme.domain.model.Solicitacao;

public interface CriaSolicitacaoService {
    Solicitacao criar(final Solicitacao solicitacao, final String chaveIdempotencia);

}
