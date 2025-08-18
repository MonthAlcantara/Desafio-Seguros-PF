package io.github.monthalcantara.acme.application.usecase;

import io.github.monthalcantara.acme.domain.enums.TipoStatus;
import io.github.monthalcantara.acme.infrastructure.persistence.entity.OutboxEntity;
import io.github.monthalcantara.acme.infrastructure.persistence.repository.OutboxRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

class RemoveOutboxEventServiceImplTest {

    private final OutboxRepository repository = mock(OutboxRepository.class);
    private final RemoveOutboxEventServiceImpl service = new RemoveOutboxEventServiceImpl(repository);

    @Test
    @DisplayName("Deve remover evento quando encontrado")
    void deveRemoverEventoQuandoEncontrado() {
        var id = UUID.randomUUID();
        var entity = new OutboxEntity();
        entity.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(entity));

        service.remover(id, TipoStatus.APROVADO.getDescricao());

        verify(repository, times(1)).delete(entity);
    }

    @Test
    @DisplayName("Não deve remover quando não encontrar evento")
    void naoDeveRemoverQuandoNaoEncontrar() {
        var id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        service.remover(id, "DESCONHECIDO");

        verify(repository, never()).delete(any());
    }
}
