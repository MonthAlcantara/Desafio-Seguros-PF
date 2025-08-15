package io.github.monthalcantara.acme.application.service;

import io.github.monthalcantara.acme.domain.model.Solicitacao;
import io.github.monthalcantara.acme.exception.SolicitacaoNaoEncontradaException;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes unitários para ConsultaSolicitacaoService")
class ConsultaSolicitacaoServiceTest {

    @Mock
    private SolicitacaoRepository repository;

    @InjectMocks
    private ConsultaSolicitacaoService service;

    @Test
    @DisplayName("porId deve retornar a solicitacao quando o ID for encontrado")
    void porId_DeveRetornarSolicitacao_QuandoIdExistir() {
        // Dado
        final var solicitacaoId = UUID.randomUUID();
        final var solicitacaoEntity = new SolicitacaoEntity();
        solicitacaoEntity.setId(solicitacaoId);
        final var solicitacaoModel = Solicitacao.builder().id(solicitacaoId).clienteId(UUID.randomUUID()).build(); // Ajustado aqui

        // Mock
        when(repository.findById(solicitacaoId)).thenReturn(Optional.of(solicitacaoEntity));

        try (MockedStatic<SolicitacaoMapper> mockedMapper = mockStatic(SolicitacaoMapper.class)) {
            mockedMapper.when(() -> SolicitacaoMapper.toModel(solicitacaoEntity)).thenReturn(solicitacaoModel);

            // Quando
            final var resultado = service.porId(solicitacaoId);

            // Então
            assertEquals(solicitacaoModel, resultado);
            verify(repository, times(1)).findById(solicitacaoId);
            mockedMapper.verify(() -> SolicitacaoMapper.toModel(solicitacaoEntity), times(1));
        }
    }

    @Test
    @DisplayName("porId deve lancar SolicitacaoNaoEncontradaException quando o ID nao for encontrado")
    void porId_DeveLancarException_QuandoIdNaoExistir() {
        // Dado
        final var solicitacaoId = UUID.randomUUID();
        when(repository.findById(solicitacaoId)).thenReturn(Optional.empty());

        // Quando e Então
        assertThrows(SolicitacaoNaoEncontradaException.class, () -> service.porId(solicitacaoId));
        verify(repository, times(1)).findById(solicitacaoId);
    }

    @Test
    @DisplayName("porClienteId deve retornar uma lista de solicitacoes quando existirem")
    void porClienteId_DeveRetornarLista_QuandoSolicitacoesExistirem() {
        // Dado
        final var clienteId = UUID.randomUUID();
        final var solicitacaoEntity1 = new SolicitacaoEntity();
        solicitacaoEntity1.setId(UUID.randomUUID());
        final var solicitacaoEntity2 = new SolicitacaoEntity();
        solicitacaoEntity2.setId(UUID.randomUUID());
        final var entities = List.of(solicitacaoEntity1, solicitacaoEntity2);

        final var solicitacaoModel1 = Solicitacao.builder().id(solicitacaoEntity1.getId()).clienteId(clienteId).build(); // Ajustado aqui
        final var solicitacaoModel2 = Solicitacao.builder().id(solicitacaoEntity2.getId()).clienteId(clienteId).build(); // Ajustado aqui
        final var expectedModels = List.of(solicitacaoModel1, solicitacaoModel2);

        // Mock
        when(repository.findByClienteId(clienteId)).thenReturn(entities);

        try (MockedStatic<SolicitacaoMapper> mockedMapper = mockStatic(SolicitacaoMapper.class)) {
            mockedMapper.when(() -> SolicitacaoMapper.toModel(solicitacaoEntity1)).thenReturn(solicitacaoModel1);
            mockedMapper.when(() -> SolicitacaoMapper.toModel(solicitacaoEntity2)).thenReturn(solicitacaoModel2);

            // Quando
            final var resultado = service.porClienteId(clienteId);

            // Então
            assertEquals(expectedModels.size(), resultado.size());
            assertEquals(expectedModels, resultado);
            verify(repository, times(1)).findByClienteId(clienteId);
        }
    }

    @Test
    @DisplayName("porClienteId deve retornar uma lista vazia quando nao houver solicitacoes")
    void porClienteId_DeveRetornarListaVazia_QuandoNaoHouverSolicitacoes() {
        // Dado
        final var clienteId = UUID.randomUUID();
        when(repository.findByClienteId(clienteId)).thenReturn(Collections.emptyList());

        // Quando
        final var resultado = service.porClienteId(clienteId);

        // Então
        assertEquals(Collections.emptyList(), resultado);
        verify(repository, times(1)).findByClienteId(clienteId);
    }
}