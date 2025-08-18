package io.github.monthalcantara.acme.application.usecase;

import io.github.monthalcantara.acme.domain.enums.TipoStatus;
import io.github.monthalcantara.acme.domain.service.RemoveOutboxEventService;
import io.github.monthalcantara.acme.domain.service.ValidadorDeRegrasAdicionaisService;
import io.github.monthalcantara.acme.exception.SolicitacaoNaoEncontradaException;
import io.github.monthalcantara.acme.exception.StatusNaoPermitidoException;
import io.github.monthalcantara.acme.infrastructure.client.fraud.dto.response.FraudCheckResponse;
import io.github.monthalcantara.acme.infrastructure.kafka.producer.OrderEventProducer;
import io.github.monthalcantara.acme.infrastructure.persistence.entity.SolicitacaoEntity;
import io.github.monthalcantara.acme.infrastructure.persistence.entity.OutboxEntity;
import io.github.monthalcantara.acme.infrastructure.persistence.repository.SolicitacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AtualizaSolicitacaoServiceImplTest {

    @Mock
    private SolicitacaoRepository solicitacaoRepository;

    @Mock
    private ValidadorDeRegrasAdicionaisService validadorDeRegrasAdicionaisService;

    @Mock
    private RemoveOutboxEventService removeOutboxEventService;

    @Mock
    private OrderEventProducer orderEventProducer;

    @InjectMocks
    private AtualizaSolicitacaoServiceImpl service;

    private UUID solicitacaoId;
    private SolicitacaoEntity solicitacaoEntity;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        solicitacaoId = UUID.randomUUID();
        solicitacaoEntity = new SolicitacaoEntity();
        solicitacaoEntity.setId(solicitacaoId);
        solicitacaoEntity.setAtualizacaoStatusNoHistorico(TipoStatus.RECEBIDO);
    }

    @Test
    @DisplayName("Deve atualizar solicitação com novo status e enviar evento")
    void deveAtualizarSolicitacao() {

        FraudCheckResponse response = new FraudCheckResponse();
        response.setClassificacao("HIGH_RISK");

        when(solicitacaoRepository.findById(solicitacaoId)).thenReturn(Optional.of(solicitacaoEntity));
        when(validadorDeRegrasAdicionaisService.validar(any(), eq("HIGH_RISK"))).thenReturn(TipoStatus.RECEBIDO);
        when(solicitacaoRepository.save(any())).thenReturn(solicitacaoEntity);

        OutboxEntity outboxEntity = new OutboxEntity();

        // Quando
        service.atualizar(solicitacaoId, outboxEntity, response);

        // Então
        verify(solicitacaoRepository).save(any(SolicitacaoEntity.class));
        verify(removeOutboxEventService).remover(eq(solicitacaoId), anyString());
        verify(orderEventProducer).send(any());
    }

    @Test
    @DisplayName("Deve cancelar solicitação e enviar evento")
    void deveCancelarSolicitacao() {

        solicitacaoEntity.setAtualizacaoStatusNoHistorico(TipoStatus.RECEBIDO);
        when(solicitacaoRepository.findById(solicitacaoId)).thenReturn(Optional.of(solicitacaoEntity));
        when(solicitacaoRepository.save(any())).thenReturn(solicitacaoEntity);

        // Quando
        service.cancelar(solicitacaoId);

        // Então
        assertEquals(TipoStatus.CANCELADA, solicitacaoEntity.getStatus());
        verify(solicitacaoRepository).save(solicitacaoEntity);
        verify(removeOutboxEventService).remover(eq(solicitacaoId), anyString());
        verify(orderEventProducer).send(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar cancelar solicitação inexistente")
    void deveLancarExcecaoQuandoSolicitacaoNaoExiste() {

        when(solicitacaoRepository.findById(solicitacaoId)).thenReturn(Optional.empty());

        // Quando e Então & Assert
        assertThrows(SolicitacaoNaoEncontradaException.class, () -> service.cancelar(solicitacaoId));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar cancelar solicitação com status final")
    void deveLancarExcecaoQuandoStatusFinal() {
        // Dado
        solicitacaoEntity.setAtualizacaoStatusNoHistorico(TipoStatus.APROVADO);
        when(solicitacaoRepository.findById(solicitacaoId)).thenReturn(Optional.of(solicitacaoEntity));

        // Quando e Então & Assert
        assertThrows(StatusNaoPermitidoException.class, () -> service.cancelar(solicitacaoId));
    }
}
