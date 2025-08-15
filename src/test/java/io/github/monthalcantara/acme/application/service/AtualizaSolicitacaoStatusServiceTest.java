package io.github.monthalcantara.acme.application.service;

import io.github.monthalcantara.acme.domain.enums.TipoStatus;
import io.github.monthalcantara.acme.domain.model.Solicitacao;
import io.github.monthalcantara.acme.exception.SolicitacaoNaoEncontradaException;
import io.github.monthalcantara.acme.exception.StatusNaoPermitidoException;
import io.github.monthalcantara.acme.infra.fraud.dto.response.FraudCheckResponse;
import io.github.monthalcantara.acme.infra.persistence.entity.SolicitacaoEntity;
import io.github.monthalcantara.acme.infra.persistence.repository.SolicitacaoRepository;
import io.github.monthalcantara.acme.mapper.SolicitacaoMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes unitários para AtualizaSolicitacaoStatusService")
class AtualizaSolicitacaoStatusServiceTest {

    @Mock
    private SolicitacaoRepository solicitacaoRepository;

    @Mock
    private ValidadorDeRegrasAdicionaisService validadorDeRegrasAdicionaisService;

    @InjectMocks
    private AtualizaSolicitacaoStatusService service;

    private SolicitacaoEntity criarSolicitacaoEntity(TipoStatus status) {
        SolicitacaoEntity entity = new SolicitacaoEntity();
        entity.setId(UUID.randomUUID());
        entity.setStatus(status);
        return entity;
    }

    private Solicitacao criarSolicitacaoModel(UUID id) {
        return Solicitacao.builder().id(id).status(TipoStatus.REJEITADO.getDescricao()).build();
    }

    private FraudCheckResponse criarFraudCheckResponse(String classificacao) {
        try {
            FraudCheckResponse response = new FraudCheckResponse();
            Field field = FraudCheckResponse.class.getDeclaredField("classificacao");
            field.setAccessible(true);
            field.set(response, classificacao);
            return response;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Falha ao criar o DTO de teste", e);
        }
    }

    @Test
    @DisplayName("atualizarStatusComBaseEmFraude deve atualizar o status quando a solicitacao for encontrada")
    void atualizarStatusComBaseEmFraude_DeveAtualizarStatus_QuandoSolicitacaoExistir() {
        // Dado
        final var solicitacaoId = UUID.randomUUID();
        final var fraudResponse = criarFraudCheckResponse("REPROVADO");
        final var solicitacaoEntity = criarSolicitacaoEntity(TipoStatus.RECEBIDO);
        final var solicitacaoModel = criarSolicitacaoModel(solicitacaoId);

        // Mock
        when(solicitacaoRepository.findById(solicitacaoId)).thenReturn(Optional.of(solicitacaoEntity));
        when(validadorDeRegrasAdicionaisService.validar(any(Solicitacao.class), eq(fraudResponse.getClassificacao())))
                .thenReturn(TipoStatus.REJEITADO);

        try (MockedStatic<SolicitacaoMapper> mockedMapper = mockStatic(SolicitacaoMapper.class)) {
            mockedMapper.when(() -> SolicitacaoMapper.toModel(solicitacaoEntity)).thenReturn(solicitacaoModel);

            // Quando
            service.atualizarStatusComBaseEmFraude(solicitacaoId, fraudResponse);

            // Então
            assertEquals(TipoStatus.REJEITADO, solicitacaoEntity.getStatus());
            verify(solicitacaoRepository, times(1)).findById(solicitacaoId);
            verify(solicitacaoRepository, times(1)).save(solicitacaoEntity);
        }
    }

    @Test
    @DisplayName("atualizarStatusComBaseEmFraude não deve fazer nada se a solicitacao não for encontrada")
    void atualizarStatusComBaseEmFraude_NaoDeveFazerNada_QuandoSolicitacaoNaoExistir() {
        // Dado
        final var solicitacaoId = UUID.randomUUID();
        final var fraudResponse = criarFraudCheckResponse("APROVADO");

        // Mock
        when(solicitacaoRepository.findById(solicitacaoId)).thenReturn(Optional.empty());

        // Quando
        service.atualizarStatusComBaseEmFraude(solicitacaoId, fraudResponse);

        // Então
        verify(solicitacaoRepository, times(1)).findById(solicitacaoId);
        verify(solicitacaoRepository, never()).save(any(SolicitacaoEntity.class));
    }


    @Test
    @DisplayName("cancelar deve atualizar o status para CANCELADA quando o status atual for válido")
    void cancelar_DeveAtualizarStatusParaCancelada_QuandoStatusForValido() {
        // Dado
        final var solicitacaoId = UUID.randomUUID();
        final var solicitacaoEntity = criarSolicitacaoEntity(TipoStatus.RECEBIDO);

        // Mock
        when(solicitacaoRepository.findById(solicitacaoId)).thenReturn(Optional.of(solicitacaoEntity));
        when(solicitacaoRepository.save(any(SolicitacaoEntity.class))).thenReturn(solicitacaoEntity);

        // Quando
        service.cancelar(solicitacaoId);

        // Então
        assertEquals(TipoStatus.CANCELADA, solicitacaoEntity.getStatus());
        assertNotNull(solicitacaoEntity.getFinalizadoEm());
        verify(solicitacaoRepository, times(1)).findById(solicitacaoId);
        verify(solicitacaoRepository, times(1)).save(solicitacaoEntity);
    }

    @Test
    @DisplayName("cancelar deve lancar SolicitacaoNaoEncontradaException quando a solicitacao nao existir")
    void cancelar_DeveLancarException_QuandoSolicitacaoNaoExistir() {
        // Dado
        final var solicitacaoId = UUID.randomUUID();
        when(solicitacaoRepository.findById(solicitacaoId)).thenReturn(Optional.empty());

        // Quando e Então
        assertThrows(SolicitacaoNaoEncontradaException.class, () -> service.cancelar(solicitacaoId));
        verify(solicitacaoRepository, times(1)).findById(solicitacaoId);
        verify(solicitacaoRepository, never()).save(any(SolicitacaoEntity.class));
    }

    @Test
    @DisplayName("cancelar deve lancar StatusNaoPermitidoException quando o status for APROVADO")
    void cancelar_DeveLancarException_QuandoStatusForAprovado() {
        // Dado
        final var solicitacaoId = UUID.randomUUID();
        final var solicitacaoEntity = criarSolicitacaoEntity(TipoStatus.APROVADO);
        when(solicitacaoRepository.findById(solicitacaoId)).thenReturn(Optional.of(solicitacaoEntity));

        // Quando e Então
        assertThrows(StatusNaoPermitidoException.class, () -> service.cancelar(solicitacaoId));
        verify(solicitacaoRepository, times(1)).findById(solicitacaoId);
        verify(solicitacaoRepository, never()).save(any(SolicitacaoEntity.class));
    }

    @Test
    @DisplayName("cancelar deve lancar StatusNaoPermitidoException quando o status for REJEITADO")
    void cancelar_DeveLancarException_QuandoStatusForRejeitado() {
        // Dado
        final var solicitacaoId = UUID.randomUUID();
        final var solicitacaoEntity = criarSolicitacaoEntity(TipoStatus.REJEITADO);
        when(solicitacaoRepository.findById(solicitacaoId)).thenReturn(Optional.of(solicitacaoEntity));

        // Quando e Então
        assertThrows(StatusNaoPermitidoException.class, () -> service.cancelar(solicitacaoId));
        verify(solicitacaoRepository, times(1)).findById(solicitacaoId);
        verify(solicitacaoRepository, never()).save(any(SolicitacaoEntity.class));
    }
}