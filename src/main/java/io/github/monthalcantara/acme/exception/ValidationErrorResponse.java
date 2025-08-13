package io.github.monthalcantara.acme.exception;

import lombok.Data;

import java.time.Instant;
import java.util.Map;
@Data
public class ValidationErrorResponse {
    private Instant timestamp;
    private int status;
    private String error;
    private Map<String, String> fieldErrors;

    public ValidationErrorResponse(Instant timestamp, int status, String error, Map<String, String> fieldErrors) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.fieldErrors = fieldErrors;
    }


}