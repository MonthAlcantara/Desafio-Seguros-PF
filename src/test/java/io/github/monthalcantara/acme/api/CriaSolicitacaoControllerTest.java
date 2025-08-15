package io.github.monthalcantara.acme.api;

import io.github.monthalcantara.acme.api.dto.request.SolicitacaoRequest;
import io.github.monthalcantara.acme.api.dto.response.SolicitacaoCriadaResponse;
import io.github.monthalcantara.acme.api.validator.SolicitacaoRequestValidator;
import io.github.monthalcantara.acme.application.service.CriaSolicitacaoService;
import io.github.monthalcantara.acme.domain.enums.TipoCanalVendas;
import io.github.monthalcantara.acme.domain.enums.TipoCategoria;
import io.github.monthalcantara.acme.domain.enums.TipoMetodoPagamento;
import io.github.monthalcantara.acme.domain.model.Solicitacao;
import io.github.monthalcantara.acme.exception.ValidacaoNegocioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DisplayName("Testes de unidade para CriaSolicitacaoController")
class CriaSolicitacaoControllerTest {

    @Mock
    private CriaSolicitacaoService criaSolicitacaoService;

    @Mock
    private SolicitacaoRequestValidator requestValidator;

    @InjectMocks
    private CriaSolicitacaoController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private SolicitacaoRequest criarPayloadValido() {
        return new SolicitacaoRequest(
                UUID.randomUUID(),
                UUID.randomUUID(),
                TipoCategoria.AUTO.toString(),
                TipoCanalVendas.MOBILE.toString(),
                TipoMetodoPagamento.CARTAO_CREDITO.getDescricao(),
                BigDecimal.valueOf(150.75),
                BigDecimal.valueOf(50000.00),
                Collections.singletonMap("Cobertura Basica", BigDecimal.valueOf(25000.00)),
                Collections.singletonList("Assistência 24h")
        );
    }

    @Test
    @DisplayName("Deve retornar 201 Created e o DTO de resposta quando a requisicao e valida")
    void deveRetornar201CreatedQuandoRequisicaoValida() {
        // Dado
        final var idempotencyKey = UUID.randomUUID().toString();
        final var request = criarPayloadValido();
        final var responseDTO = Mockito.mock(Solicitacao.class);

        doNothing().when(requestValidator).validar(any(SolicitacaoRequest.class));
        when(criaSolicitacaoService.criar(any(Solicitacao.class), eq(idempotencyKey))).thenReturn(responseDTO);

        // Quando
        ResponseEntity<SolicitacaoCriadaResponse> responseEntity = controller.criarApolice(request, idempotencyKey);

        // Então
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(responseDTO.getId(), responseEntity.getBody().getId());
    }

    @Test
    @DisplayName("Deve lancar ValidacaoNegocioException quando a validacao personalizada falha")
    void deveLancarExceptionQuandoValidacaoPersonalizadaFalha() {
        // Dado
        final var idempotencyKey = UUID.randomUUID().toString();
        final var request = criarPayloadValido();

        doThrow(new ValidacaoNegocioException(List.of("Erros de validação de negócio"))).when(requestValidator).validar(any(SolicitacaoRequest.class));

        // Quando e Então
        ValidacaoNegocioException exception = assertThrows(ValidacaoNegocioException.class, () -> controller.criarApolice(request, idempotencyKey));

        assertEquals("Erros de validação de negócio", exception.getMessage());
    }
}