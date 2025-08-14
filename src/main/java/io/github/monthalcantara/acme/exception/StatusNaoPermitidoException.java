package io.github.monthalcantara.acme.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class StatusNaoPermitidoException extends RuntimeException {
    public StatusNaoPermitidoException(String message) {
        super(message);
    }
}