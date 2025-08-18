package io.github.monthalcantara.acme.infrastructure.scheduler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.monthalcantara.acme.TestUtils;
import io.github.monthalcantara.acme.domain.model.Solicitacao;
import io.github.monthalcantara.acme.domain.service.AtualizaSolicitacaoService;
import io.github.monthalcantara.acme.infrastructure.client.fraud.FraudClient;
import io.github.monthalcantara.acme.infrastructure.client.fraud.dto.response.FraudCheckResponse;
import io.github.monthalcantara.acme.infrastructure.persistence.entity.OutboxEntity;
import io.github.monthalcantara.acme.infrastructure.persistence.repository.OutboxRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class FraudCheckSchedulerIT {

    @Autowired
    private OutboxRepository outboxRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FraudCheckScheduler scheduler;

    @MockBean
    private FraudClient fraudClient;

    @MockBean
    private AtualizaSolicitacaoService atualizaSolicitacaoService;

    @Test
    @DisplayName("Deve processar evento do outbox e chamar FraudClient + AtualizaSolicitacaoService")
    void deveProcessarEventoDoOutbox() throws Exception {

        var solicitacao = new Solicitacao();
        solicitacao.setId(UUID.randomUUID());

        String conteudoJson = objectMapper.writeValueAsString(solicitacao);

        var outbox = new OutboxEntity(
                solicitacao.getId(),
                UUID.randomUUID().toString(),
                "acme-solicitacao-criada",
                "acme-solicitacao-criada",
                conteudoJson
        );
        outboxRepository.save(outbox);

        String payloadResponse = TestUtils.lerPayloadDoArquivo("fraud-solicitacao-valida.json");
        // ✅ usa o objectMapper injetado pelo Spring
        FraudCheckResponse response = objectMapper.readValue(payloadResponse, FraudCheckResponse.class);
        when(fraudClient.checkFraud(any())).thenReturn(response);

        // Quando
        scheduler.processOutboxForFraudCheck();

        // Então
        verify(fraudClient, times(1)).checkFraud(any(Solicitacao.class));
        verify(atualizaSolicitacaoService, times(1)).atualizar(eq(solicitacao.getId()), any(), any());
    }
}
