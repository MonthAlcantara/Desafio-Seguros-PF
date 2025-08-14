package io.github.monthalcantara.acme.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class ValidacaoNegocioException extends RuntimeException {
    private final List<String> erros;

    public ValidacaoNegocioException(final List<String> erros) {
        super("Erros de validação de negócio");
        this.erros = erros;
    }
}
