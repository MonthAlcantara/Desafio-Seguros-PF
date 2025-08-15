package io.github.monthalcantara.acme.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SolicitacaoNaoEncontradaException extends RuntimeException {
    public SolicitacaoNaoEncontradaException(final String message) {
        super(message);
    }

    public SolicitacaoNaoEncontradaException(final UUID id) {
        super("Solicitacao não encontrada para o ID: " + id);
    }
}