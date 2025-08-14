package io.github.monthalcantara.acme.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(final MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        final var response = new ValidationErrorResponse(Instant.now(), HttpStatus.BAD_REQUEST.value(), "Erro de validação", errors);

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ValidacaoNegocioException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessValidation(final ValidacaoNegocioException ex) {
        return ResponseEntity.badRequest().body(Map.of("erros", ex.getErros()));
    }

    @ExceptionHandler(SolicitacaoNaoEncontradaException.class)
    public ResponseEntity<ErrorResponse> handleSolicitacaoNaoEncontrada(SolicitacaoNaoEncontradaException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .dataHora(Instant.now())
                .codigoStatus(HttpStatus.NOT_FOUND.value())
                .mensagemErro(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(StatusNaoPermitidoException.class)
    public ResponseEntity<ErrorResponse> handleStatusNaoPermitido(StatusNaoPermitidoException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .dataHora(Instant.now())
                .codigoStatus(HttpStatus.BAD_REQUEST.value())
                .mensagemErro(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String mensagemErro;
        if (ex.getRequiredType() != null && ex.getRequiredType().equals(UUID.class)) {
            mensagemErro = "O ID da solicitação '" + ex.getValue() + "' não é um UUID válido.";
        } else {
            mensagemErro = "Ocorreu um erro de tipo de argumento: " + ex.getMessage();
        }

        ErrorResponse error = ErrorResponse.builder()
                .dataHora(Instant.now())
                .codigoStatus(HttpStatus.BAD_REQUEST.value())
                .mensagemErro(mensagemErro)
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(final IllegalArgumentException e) {
        final var response = new ErrorResponse(Instant.now(), HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(final Exception e) {
        final var response = new ErrorResponse(Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro interno do servidor");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
