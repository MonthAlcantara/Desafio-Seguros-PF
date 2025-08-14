package io.github.monthalcantara.acme.exception;

import java.time.Instant;
import java.util.Map;

public class ValidationErrorResponse {
    private Instant timestamp;
    private int status;
    private String error;
    private Map<String, String> fieldErrors;

    public ValidationErrorResponse(final Instant timestamp, final int status, final String error, final Map<String, String> fieldErrors) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.fieldErrors = fieldErrors;
    }
}