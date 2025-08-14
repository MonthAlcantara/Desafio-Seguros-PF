package io.github.monthalcantara.acme.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SolicitacaoNaoEncontradaException extends RuntimeException {
    public SolicitacaoNaoEncontradaException(String message) {
        super(message);
    }
}