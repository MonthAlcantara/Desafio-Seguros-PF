package io.github.monthalcantara.acme.application.usecase;

import io.github.monthalcantara.acme.domain.model.Solicitacao;
import io.github.monthalcantara.acme.exception.SolicitacaoNaoEncontradaException;
import io.github.monthalcantara.acme.infrastructure.persistence.entity.SolicitacaoEntity;
import io.github.monthalcantara.acme.infrastructure.persistence.repository.SolicitacaoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultaSolicitacaoServiceImplTest {

    @Mock
    private SolicitacaoRepository solicitacaoRepository;

    @InjectMocks
    private ConsultaSolicitacaoServiceImpl consultaSolicitacaoService;

    @Test
    @DisplayName("Deve buscar uma solicitação por ID com sucesso")
    void deveBuscarSolicitacaoPorIdComSucesso() {

        UUID id = UUID.randomUUID();
        SolicitacaoEntity entity = new SolicitacaoEntity();
        entity.setId(id);
        when(solicitacaoRepository.findById(id)).thenReturn(Optional.of(entity));


        Solicitacao resultado = consultaSolicitacaoService.porId(id);


        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        verify(solicitacaoRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Deve lançar uma exceção ao buscar uma solicitação por ID que não existe")
    void deveLancarExcecaoQuandoSolicitacaoNaoExistePorId() {

        UUID id = UUID.randomUUID();
        when(solicitacaoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(SolicitacaoNaoEncontradaException.class, () -> consultaSolicitacaoService.porId(id));
        verify(solicitacaoRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Deve buscar uma solicitação por client ID com sucesso")
    void deveBuscarSolicitacaoPorClienteIdComSucesso() {

        UUID clienteId = UUID.randomUUID();
        SolicitacaoEntity entity = new SolicitacaoEntity();
        entity.setClienteId(clienteId);
        when(solicitacaoRepository.findByClienteId(clienteId)).thenReturn(List.of(entity));


        final var resultados = consultaSolicitacaoService.porClienteId(clienteId);


        assertNotNull(resultados);
        resultados.forEach(solicitacao -> {
            assertNotNull(solicitacao);
            assertEquals(clienteId, solicitacao.getClienteId());
        });
        verify(solicitacaoRepository, times(1)).findByClienteId(clienteId);
    }
}