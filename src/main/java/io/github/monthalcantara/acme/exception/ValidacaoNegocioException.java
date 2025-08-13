package io.github.monthalcantara.acme.exception;

import java.util.List;

public class ValidacaoNegocioException extends RuntimeException {
    private final List<String> erros;

    public ValidacaoNegocioException(List<String> erros) {
        super("Erros de validação de negócio");
        this.erros = erros;
    }

    public List<String> getErros() {
        return erros;
    }
}
