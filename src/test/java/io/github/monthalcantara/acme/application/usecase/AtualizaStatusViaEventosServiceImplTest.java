package io.github.monthalcantara.acme.application.usecase;

import io.github.monthalcantara.acme.domain.enums.TipoStatus;
import io.github.monthalcantara.acme.exception.SolicitacaoNaoEncontradaException;
import io.github.monthalcantara.acme.infrastructure.kafka.producer.OrderEventProducer;
import io.github.monthalcantara.acme.infrastructure.persistence.entity.SolicitacaoEntity;
import io.github.monthalcantara.acme.infrastructure.persistence.repository.SolicitacaoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AtualizaStatusViaEventosServiceImplTest {

    private final SolicitacaoRepository repository = mock(SolicitacaoRepository.class);
    private final OrderEventProducer producer = mock(OrderEventProducer.class);
    private final AtualizaStatusViaEventosServiceImpl service = new AtualizaStatusViaEventosServiceImpl(repository, producer);

    @Test
    @DisplayName("Deve atualizar status mediante pagamento")
    void deveAtualizarStatusPagamento() {
        var id = UUID.randomUUID();
        var entity = new SolicitacaoEntity();
        entity.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(entity));

        service.atualizaStatusMediantePagamento(id, TipoStatus.APROVADO.getDescricao());

        verify(repository, times(1)).save(entity);
        verify(producer, times(1)).send(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando solicitação não encontrada")
    void deveLancarExcecaoQuandoNaoEncontrar() {
        var id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(SolicitacaoNaoEncontradaException.class,
                () -> service.atualizaStatusMediantePagamento(id, "APROVADO"));
    }
}
