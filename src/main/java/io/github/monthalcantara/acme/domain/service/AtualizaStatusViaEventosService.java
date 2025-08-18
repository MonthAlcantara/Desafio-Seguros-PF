package io.github.monthalcantara.acme.domain.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

public interface AtualizaStatusViaEventosService {
    void atualizaStatusMediantePagamento(UUID solicitacaoId, String status);

    void atualizaStatusMedianteSubscricao(UUID solicitacaoId, String status);

}
