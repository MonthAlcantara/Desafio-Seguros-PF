package io.github.monthalcantara.acme.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ErroInternoException extends RuntimeException {
    public ErroInternoException(String message, Exception e) {
        super(message, e);
    }
}