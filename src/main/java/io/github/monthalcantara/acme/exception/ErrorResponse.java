package io.github.monthalcantara.acme.exception;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class ErrorResponse {

    @JsonProperty("timestamp")
    private Instant dataHora;

    @JsonProperty("status")
    private int codigoStatus;

    @JsonProperty("error")
    private String mensagemErro;

    public ErrorResponse(final Instant dataHora, final int codigoStatus, final String mensagemErro) {
        this.dataHora = dataHora;
        this.codigoStatus = codigoStatus;
        this.mensagemErro = mensagemErro;
    }
}
