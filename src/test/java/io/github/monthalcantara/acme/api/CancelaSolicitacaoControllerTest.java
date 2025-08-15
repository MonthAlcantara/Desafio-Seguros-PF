package io.github.monthalcantara.acme.api;

import io.github.monthalcantara.acme.application.service.AtualizaSolicitacaoStatusService;
import io.github.monthalcantara.acme.exception.SolicitacaoNaoEncontradaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@DisplayName("Testes de unidade para CancelaSolicitacaoController")
class CancelaSolicitacaoControllerTest {

    @Mock
    private AtualizaSolicitacaoStatusService atualizaSolicitacaoStatusService;

    @InjectMocks
    private CancelaSolicitacaoController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve retornar 204 No Content quando a solicitacao for cancelada com sucesso")
    void deveRetornar204NoContentQuandoCancelamentoSucesso() {
        // Dado
        final var id = UUID.randomUUID();
        doNothing().when(atualizaSolicitacaoStatusService).cancelar(id);

        // Quando
        final ResponseEntity<Void> responseEntity = controller.cancelarSolicitacao(id);

        // Então
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(atualizaSolicitacaoStatusService, times(1)).cancelar(id);
    }

    @Test
    @DisplayName("Deve lancar SolicitacaoNaoEncontradaException quando a solicitacao nao for encontrada")
    void deveLancarSolicitacaoNaoEncontradaExceptionQuandoSolicitacaoNaoEncontrada() {
        // Dado
        final var id = UUID.randomUUID();
        doThrow(new SolicitacaoNaoEncontradaException("Solicitação não encontrada")).when(atualizaSolicitacaoStatusService).cancelar(any(UUID.class));

        // Quando e Entao
        final SolicitacaoNaoEncontradaException exception = assertThrows(SolicitacaoNaoEncontradaException.class, () -> controller.cancelarSolicitacao(id));

        assertEquals("Solicitação não encontrada", exception.getMessage());
        verify(atualizaSolicitacaoStatusService, times(1)).cancelar(id);
    }
}