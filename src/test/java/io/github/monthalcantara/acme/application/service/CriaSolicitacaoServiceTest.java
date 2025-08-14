package io.github.monthalcantara.acme.application.service;

import io.github.monthalcantara.acme.domain.enums.TipoStatus;
import io.github.monthalcantara.acme.domain.model.Solicitacao;
import io.github.monthalcantara.acme.infra.persistence.entity.SolicitacaoEntity;
import io.github.monthalcantara.acme.infra.persistence.repository.SolicitacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes de unidade para CriaSolicitacaoService")
class CriaSolicitacaoServiceTest {

    @Mock
    private SolicitacaoRepository repository;
    @Mock
    private FraudNotificationService fraudNotificationService;
    @InjectMocks
    private CriaSolicitacaoService criaSolicitacaoService;

    private Solicitacao solicitacaoInput;
    private SolicitacaoEntity solicitacaoEntity;
    private final String chaveIdempotencia = "chave-idempotencia-test";

    @BeforeEach
    void setUp() {
        solicitacaoInput = Solicitacao.builder().build();
        solicitacaoEntity = SolicitacaoEntity.builder()
                .id(UUID.randomUUID())
                .status(TipoStatus.RECEBIDO)
                .build();
    }

    @Test
    @DisplayName("Deve criar uma nova solicitacao quando a chave de idempotencia nao existir")
    void deveCriarNovaSolicitacaoQuandoChaveNaoExistir() {
        // Dado
        when(repository.findByChaveIdempotencia(anyString())).thenReturn(Optional.empty());
        when(repository.save(any(SolicitacaoEntity.class))).thenReturn(solicitacaoEntity);

        // Quando
        final Solicitacao resultado = criaSolicitacaoService.criar(solicitacaoInput, chaveIdempotencia);

        // Então
        assertNotNull(resultado);
        assertEquals(solicitacaoEntity.getId(), resultado.getId());
        assertEquals(solicitacaoEntity.getStatus().getDescricao(), resultado.getStatus());
        verify(repository, times(1)).findByChaveIdempotencia(chaveIdempotencia);
        verify(repository, times(1)).save(any(SolicitacaoEntity.class));
        verify(fraudNotificationService, times(1)).notifyAsync(any(Solicitacao.class));
    }

    @Test
    @DisplayName("Deve retornar solicitacao existente quando a chave de idempotencia ja existir")
    void deveRetornarSolicitacaoExistenteQuandoChaveExistir() {
        // Dado
        when(repository.findByChaveIdempotencia(anyString())).thenReturn(Optional.of(solicitacaoEntity));

        // Quando
        final Solicitacao resultado = criaSolicitacaoService.criar(solicitacaoInput, chaveIdempotencia);

        // Então
        assertNotNull(resultado);
        assertEquals(solicitacaoEntity.getId(), resultado.getId());
        assertEquals(solicitacaoEntity.getStatus().getDescricao(), resultado.getStatus());
        verify(repository, times(1)).findByChaveIdempotencia(chaveIdempotencia);
        verify(repository, never()).save(any(SolicitacaoEntity.class));
        verify(fraudNotificationService, times(1)).notifyAsync(any(Solicitacao.class));
    }

    @Test
    @DisplayName("Nao deve acionar servico de fraude se o status da solicitacao nao for 'RECEBIDO'")
    void naoDeveAcionarServicoDeFraudeQuandoStatusDiferenteDeRecebido() {
        // Dado
        final SolicitacaoEntity solicitacaoRejeitada = SolicitacaoEntity.builder()
                .id(UUID.randomUUID())
                .status(TipoStatus.REJEITADO)
                .build();
        when(repository.findByChaveIdempotencia(anyString())).thenReturn(Optional.of(solicitacaoRejeitada));

        // Quando
        final Solicitacao resultado = criaSolicitacaoService.criar(solicitacaoInput, chaveIdempotencia);

        // Então
        assertNotNull(resultado);
        assertEquals(solicitacaoRejeitada.getStatus().getDescricao(), resultado.getStatus());
        verify(fraudNotificationService, never()).notifyAsync(any(Solicitacao.class));
    }
}