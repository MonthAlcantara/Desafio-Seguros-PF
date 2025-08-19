package io.github.monthalcantara.acme.infrastructure.web;

import io.github.monthalcantara.acme.domain.model.Solicitacao;
import io.github.monthalcantara.acme.domain.service.CriaSolicitacaoService;
import io.github.monthalcantara.acme.exception.ValidacaoNegocioException;
import io.github.monthalcantara.acme.infrastructure.web.dto.request.SolicitacaoRequest;
import io.github.monthalcantara.acme.infrastructure.web.dto.response.SolicitacaoCriadaResponse;
import io.github.monthalcantara.acme.util.validator.SolicitacaoRequestValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CriaSolicitacaoControllerTest {

    @Mock
    private CriaSolicitacaoService criaSolicitacaoService;

    @Mock
    private SolicitacaoRequestValidator requestValidator;

    @InjectMocks
    private CriaSolicitacaoController controller;

    @Test
    @DisplayName("Deve criar solicitação com sucesso")
    void deveCriarSolicitacaoComSucesso() {

        SolicitacaoRequest request = new SolicitacaoRequest();
        String idempotencyKey = UUID.randomUUID().toString();
        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setId(UUID.randomUUID());

        when(criaSolicitacaoService.criar(any(Solicitacao.class), eq(idempotencyKey))).thenReturn(solicitacao);


        ResponseEntity<SolicitacaoCriadaResponse> response = controller.criarApolice(request, idempotencyKey);


        assertNotNull(response);
        assertEquals(201, response.getStatusCode().value());
        assertEquals(solicitacao.getId(), response.getBody().getId());
        verify(requestValidator).validar(request);
        verify(criaSolicitacaoService).criar(any(Solicitacao.class), eq(idempotencyKey));
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar request inválido")
    void deveLancarExcecaoQuandoRequestInvalido() {

        SolicitacaoRequest request = new SolicitacaoRequest();
        String idempotencyKey = UUID.randomUUID().toString();

        doThrow(new ValidacaoNegocioException(java.util.List.of("Erro"))).when(requestValidator).validar(request);

        assertThrows(ValidacaoNegocioException.class, () -> controller.criarApolice(request, idempotencyKey));
        verify(requestValidator).validar(request);
        verifyNoInteractions(criaSolicitacaoService);
    }
}
