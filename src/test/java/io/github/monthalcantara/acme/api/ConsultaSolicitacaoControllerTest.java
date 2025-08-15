package io.github.monthalcantara.acme.api;

import io.github.monthalcantara.acme.api.dto.response.SolicitacaoResponse;
import io.github.monthalcantara.acme.application.service.ConsultaSolicitacaoService;
import io.github.monthalcantara.acme.domain.model.Solicitacao;
import io.github.monthalcantara.acme.exception.SolicitacaoNaoEncontradaException;
import io.github.monthalcantara.acme.mapper.SolicitacaoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DisplayName("Testes de unidade para ConsultaSolicitacaoController")
class ConsultaSolicitacaoControllerTest {

    @Mock
    private ConsultaSolicitacaoService consultaSolicitacaoService;

    @InjectMocks
    private ConsultaSolicitacaoController controller;

    private Solicitacao solicitacaoMock;
    private SolicitacaoResponse solicitacaoResponseMock;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        solicitacaoMock = Solicitacao.builder()
                .id(UUID.randomUUID())
                .status("RECEBIDO")
                .build();
        solicitacaoResponseMock = SolicitacaoMapper.toResponse(solicitacaoMock);
    }

    @Test
    @DisplayName("Deve retornar 200 OK e a solicitacao quando o id for encontrado")
    void deveRetornar200OkESolicitacaoQuandoIdEncontrado() {
        // Dado
        final var id = solicitacaoMock.getId();
        when(consultaSolicitacaoService.porId(id)).thenReturn(solicitacaoMock);

        // Quando
        final ResponseEntity<SolicitacaoResponse> responseEntity = controller.porId(id);

        // Entao
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(solicitacaoResponseMock.getId(), responseEntity.getBody().getId());
        assertEquals(solicitacaoResponseMock.getStatus(), responseEntity.getBody().getStatus());
    }

    @Test
    @DisplayName("Deve lancar NotFoundException quando a solicitacao por id nao for encontrada")
    void deveLancarNotFoundExceptionQuandoSolicitacaoPorIdNaoEncontrada() {
        // Dado
        final var id = UUID.randomUUID();
        when(consultaSolicitacaoService.porId(id)).thenThrow(new SolicitacaoNaoEncontradaException("Solicitação não encontrada"));

        // Quando e Entao
        assertThrows(SolicitacaoNaoEncontradaException.class, () -> controller.porId(id));
    }

    @Test
    @DisplayName("Deve retornar 200 OK e a lista de solicitacoes quando o cliente id for encontrado")
    void deveRetornar200OkEListaDeSolicitacoesQuandoClienteIdEncontrado() {
        // Dado
        final var clienteId = UUID.randomUUID();
        final var listaSolicitacoes = Collections.singletonList(solicitacaoMock);
        final var listaSolicitacoesResponse = SolicitacaoMapper.toResponseList(listaSolicitacoes);

        when(consultaSolicitacaoService.porClienteId(clienteId)).thenReturn(listaSolicitacoes);

        // Quando
        final ResponseEntity<List<SolicitacaoResponse>> responseEntity = controller.porClienteId(clienteId);

        // Entao
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(listaSolicitacoesResponse.size(), responseEntity.getBody().size());
        assertEquals(listaSolicitacoesResponse.get(0).getId(), responseEntity.getBody().get(0).getId());
    }

    @Test
    @DisplayName("Deve lancar NotFoundException quando a lista de solicitacoes por cliente id nao for encontrada")
    void deveLancarNotFoundExceptionQuandoListaSolicitacoesNaoEncontrada() {
        // Dado
        final var clienteId = UUID.randomUUID();
        when(consultaSolicitacaoService.porClienteId(clienteId)).thenThrow(new SolicitacaoNaoEncontradaException("Nenhuma solicitação encontrada para o cliente"));

        // Quando e Entao
        assertThrows(SolicitacaoNaoEncontradaException.class, () -> controller.porClienteId(clienteId));
    }
}