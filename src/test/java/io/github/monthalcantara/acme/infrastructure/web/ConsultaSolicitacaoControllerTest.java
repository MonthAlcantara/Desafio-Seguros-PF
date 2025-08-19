package io.github.monthalcantara.acme.infrastructure.web;

import io.github.monthalcantara.acme.domain.model.Solicitacao;
import io.github.monthalcantara.acme.domain.service.ConsultaSolicitacaoService;
import io.github.monthalcantara.acme.infrastructure.web.dto.response.SolicitacaoResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsultaSolicitacaoControllerTest {

    @Mock
    private ConsultaSolicitacaoService consultaSolicitacaoService;

    @InjectMocks
    private ConsultaSolicitacaoController controller;

    @Test
    @DisplayName("Deve buscar solicitação por ID com sucesso")
    void deveBuscarPorIdComSucesso() {

        final var id = UUID.randomUUID();
        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setId(id);

        when(consultaSolicitacaoService.porId(id)).thenReturn(solicitacao);


        ResponseEntity<SolicitacaoResponse> response = controller.porId(id);


        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(id, response.getBody().getId());
    }

    @Test
    @DisplayName("Deve retornar 404 quando solicitação não encontrada")
    void deveRetornar404QuandoNaoEncontrado() {
        final var id = UUID.randomUUID();

        when(consultaSolicitacaoService.porId(id)).thenReturn(new Solicitacao());

        ResponseEntity<SolicitacaoResponse> response = controller.porId(id);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Deve buscar solicitações por cliente com sucesso")
    void deveBuscarPorClienteComSucesso() {
        final var clienteId = UUID.randomUUID();
        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setId(UUID.randomUUID());

        when(consultaSolicitacaoService.porClienteId(clienteId)).thenReturn(List.of(solicitacao));

        ResponseEntity<List<SolicitacaoResponse>> response = controller.porClienteId(clienteId);

        assertEquals(200, response.getStatusCodeValue());
        assertFalse(response.getBody().isEmpty());
    }
}
