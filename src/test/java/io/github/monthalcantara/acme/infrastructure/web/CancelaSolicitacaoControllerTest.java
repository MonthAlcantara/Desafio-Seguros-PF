package io.github.monthalcantara.acme.infrastructure.web;

import io.github.monthalcantara.acme.domain.service.AtualizaSolicitacaoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CancelaSolicitacaoControllerTest {

    @Mock
    private AtualizaSolicitacaoService atualizaSolicitacaoService;

    @InjectMocks
    private CancelaSolicitacaoController controller;

    @Test
    @DisplayName("Deve cancelar solicitação com sucesso")
    void deveCancelarSolicitacaoComSucesso() {

        final var id = UUID.randomUUID();

        Mockito.doNothing().when(atualizaSolicitacaoService).cancelar(id);


        ResponseEntity<Void> response = controller.cancelarSolicitacao(id);


        assertEquals(204, response.getStatusCode().value());
        assertNull(response.getBody());
        verify(atualizaSolicitacaoService).cancelar(id);
    }
}
