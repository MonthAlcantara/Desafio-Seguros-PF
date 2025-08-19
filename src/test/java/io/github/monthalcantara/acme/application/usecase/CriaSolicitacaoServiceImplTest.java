package io.github.monthalcantara.acme.application.usecase;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.monthalcantara.acme.domain.enums.TipoStatus;
import io.github.monthalcantara.acme.domain.model.Solicitacao;
import io.github.monthalcantara.acme.infrastructure.kafka.producer.OrderEventProducer;
import io.github.monthalcantara.acme.infrastructure.persistence.entity.OutboxEntity;
import io.github.monthalcantara.acme.infrastructure.persistence.entity.SolicitacaoEntity;
import io.github.monthalcantara.acme.infrastructure.persistence.repository.OutboxRepository;
import io.github.monthalcantara.acme.infrastructure.persistence.repository.SolicitacaoRepository;
import io.github.monthalcantara.acme.util.mapper.SolicitacaoMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class CriaSolicitacaoServiceImplTest {

    @Mock
    private SolicitacaoRepository solicitacaoRepository;

    @Mock
    private OutboxRepository outboxRepository;

    @Mock
    private OrderEventProducer orderEventProducer;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private CriaSolicitacaoServiceImpl criaSolicitacaoService;

    @Test
    @DisplayName("Deve criar uma nova solicitação com sucesso")
    void deveCriarUmaNovaSolicitacaoComSucesso() throws Exception {

        String chaveIdempotencia = UUID.randomUUID().toString();
        Solicitacao solicitacao = Solicitacao.builder()
                .status(TipoStatus.RECEBIDO.getDescricao())
                .build();

        SolicitacaoEntity solicitacaoSalva = SolicitacaoMapper.toEntity(solicitacao);
        solicitacaoSalva.setId(UUID.randomUUID());
        solicitacaoSalva.setStatus(TipoStatus.RECEBIDO);

        when(solicitacaoRepository.findByChaveIdempotencia(chaveIdempotencia)).thenReturn(Optional.empty());
        when(solicitacaoRepository.save(any(SolicitacaoEntity.class))).thenReturn(solicitacaoSalva);
        when(objectMapper.writeValueAsString(any())).thenReturn("{}");


        Solicitacao resultado = criaSolicitacaoService.criar(solicitacao, chaveIdempotencia);


        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        verify(solicitacaoRepository, times(1)).save(any(SolicitacaoEntity.class));
        verify(outboxRepository, times(1)).save(any(OutboxEntity.class));
        verify(orderEventProducer, times(1)).send(any());
    }


    @Test
    @DisplayName("Deve retornar uma solicitação existente se a chave de idempotência já existir")
    void deveRetornarSolicitacaoExistenteQuandoChaveIdempotenciaJaExistir() {

        String chaveIdempotencia = UUID.randomUUID().toString();
        Solicitacao solicitacao = Solicitacao.builder().build();
        SolicitacaoEntity solicitacaoExistente = SolicitacaoMapper.toEntity(solicitacao);
        solicitacaoExistente.setChaveIdempotencia(chaveIdempotencia);
        solicitacaoExistente.setId(UUID.randomUUID());

        when(solicitacaoRepository.findByChaveIdempotencia(chaveIdempotencia)).thenReturn(Optional.of(solicitacaoExistente));


        Solicitacao resultado = criaSolicitacaoService.criar(solicitacao, chaveIdempotencia);


        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        verify(solicitacaoRepository, times(0)).save(any(SolicitacaoEntity.class));
        verify(outboxRepository, times(0)).save(any(OutboxEntity.class));
        verify(orderEventProducer, times(0)).send(any());
    }
}
