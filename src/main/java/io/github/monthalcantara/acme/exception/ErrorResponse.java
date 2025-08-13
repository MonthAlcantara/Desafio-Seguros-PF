package io.github.monthalcantara.acme.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Instant;

@Data
public class ErrorResponse {

    @JsonProperty("timestamp")
    private Instant dataHora;

    @JsonProperty("status")
    private int codigoStatus;

    @JsonProperty("error")
    private String mensagemErro;

    public ErrorResponse(Instant dataHora, int codigoStatus, String mensagemErro) {
        this.dataHora = dataHora;
        this.codigoStatus = codigoStatus;
        this.mensagemErro = mensagemErro;
    }
}
