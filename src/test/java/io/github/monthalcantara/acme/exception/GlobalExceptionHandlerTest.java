package io.github.monthalcantara.acme.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes unitários para GlobalExceptionHandler")
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    @DisplayName("handleValidationExceptions deve retornar BAD_REQUEST para MethodArgumentNotValidException")
    void handleValidationExceptions_DeveRetornarBadRequest_QuandoMethodArgumentNotValidException() {
        // Dado: Criação de um mock para a exceção com erros de validação
        final var bindingResult = mock(BindingResult.class);
        final var fieldError = new FieldError("objectName", "campo", "mensagem de erro");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));
        final var ex = new MethodArgumentNotValidException(null, bindingResult);

        // Quando: O handler é invocado com a exceção
        final var responseEntity = globalExceptionHandler.handleValidationExceptions(ex);
        final var body = responseEntity.getBody();

        // Então: Verifica se a resposta está correta
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(body);
    }

    @Test
    @DisplayName("handleBusinessValidation deve retornar BAD_REQUEST para ValidacaoNegocioException")
    void handleBusinessValidation_DeveRetornarBadRequest_QuandoValidacaoNegocioException() {
        // Dado: Criação da exceção de negócio
        final var erros = List.of("Erro 1 de negócio", "Erro 2 de negócio");
        final var ex = new ValidacaoNegocioException(erros);

        // Quando
        final var responseEntity = globalExceptionHandler.handleBusinessValidation(ex);

        // Então
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(erros, responseEntity.getBody().get("erros"));
    }

    @Test
    @DisplayName("handleSolicitacaoNaoEncontrada deve retornar NOT_FOUND para SolicitacaoNaoEncontradaException")
    void handleSolicitacaoNaoEncontrada_DeveRetornarNotFound_QuandoSolicitacaoNaoEncontradaException() {
        // Dado
        final var mensagem = "Solicitação não encontrada: " + UUID.randomUUID();
        final var ex = new SolicitacaoNaoEncontradaException(mensagem);

        // Quando
        final var responseEntity = globalExceptionHandler.handleSolicitacaoNaoEncontrada(ex);
        final var body = responseEntity.getBody();

        // Então
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertNotNull(body);
        assertEquals(HttpStatus.NOT_FOUND.value(), body.getCodigoStatus());
        assertEquals(mensagem, body.getMensagemErro());
    }

    @Test
    @DisplayName("handleStatusNaoPermitido deve retornar BAD_REQUEST para StatusNaoPermitidoException")
    void handleStatusNaoPermitido_DeveRetornarBadRequest_QuandoStatusNaoPermitidoException() {
        // Dado
        final var mensagem = "Não é possível cancelar uma solicitação com status APROVADO";
        final var ex = new StatusNaoPermitidoException(mensagem);

        // Quando
        final var responseEntity = globalExceptionHandler.handleStatusNaoPermitido(ex);
        final var body = responseEntity.getBody();

        // Então
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(body);
        assertEquals(HttpStatus.BAD_REQUEST.value(), body.getCodigoStatus());
        assertEquals(mensagem, body.getMensagemErro());
    }

    @Test
    @DisplayName("handleMethodArgumentTypeMismatch deve retornar BAD_REQUEST com mensagem customizada para UUID inválido")
    void handleMethodArgumentTypeMismatch_DeveRetornarBadRequest_QuandoUuidInvalido() {
        // Dado: Criação de um mock para a exceção de UUID inválido
        final var ex = mock(MethodArgumentTypeMismatchException.class);
        when(ex.getRequiredType()).thenReturn((Class) UUID.class);
        when(ex.getValue()).thenReturn("id-invalido");
        final var mensagemEsperada = "O ID da solicitação 'id-invalido' não é um UUID válido.";

        // Quando
        final var responseEntity = globalExceptionHandler.handleMethodArgumentTypeMismatch(ex);
        final var body = responseEntity.getBody();

        // Então
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(body);
        assertEquals(HttpStatus.BAD_REQUEST.value(), body.getCodigoStatus());
        assertEquals(mensagemEsperada, body.getMensagemErro());
    }

    @Test
    @DisplayName("handleIllegalArgument deve retornar BAD_REQUEST para IllegalArgumentException")
    void handleIllegalArgument_DeveRetornarBadRequest_QuandoIllegalArgumentException() {
        // Dado
        final var mensagem = "Argumento inválido";
        final var ex = new IllegalArgumentException(mensagem);

        // Quando
        final var responseEntity = globalExceptionHandler.handleIllegalArgument(ex);
        final var body = responseEntity.getBody();

        // Então
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(body);
        assertEquals(HttpStatus.BAD_REQUEST.value(), body.getCodigoStatus());
        assertEquals(mensagem, body.getMensagemErro());
    }

    @Test
    @DisplayName("handleGeneralException deve retornar INTERNAL_SERVER_ERROR para Exception genérica")
    void handleGeneralException_DeveRetornarInternalServerError_QuandoExceptionGenerica() {
        // Dado
        final var ex = new Exception("Erro inesperado");

        // Quando
        final var responseEntity = globalExceptionHandler.handleGeneralException(ex);
        final var body = responseEntity.getBody();

        // Então
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNotNull(body);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), body.getCodigoStatus());
        assertEquals("Erro interno do servidor", body.getMensagemErro());
    }
}
