package io.github.monthalcantara.acme.domain.service;

import io.github.monthalcantara.acme.domain.enums.TipoStatus;
import io.github.monthalcantara.acme.domain.model.Solicitacao;

public interface ValidadorDeRegrasAdicionaisService {
    TipoStatus validar(final Solicitacao solicitacao, final String classificacaoFraude);
}