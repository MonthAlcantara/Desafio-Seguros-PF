package io.github.monthalcantara.acme.infrastructure.web;

import io.github.monthalcantara.acme.infrastructure.client.fraud.FraudClient;
import io.github.monthalcantara.acme.infrastructure.client.fraud.dto.response.FraudCheckResponse;
import io.github.monthalcantara.acme.infrastructure.kafka.producer.OrderEventProducer;
import io.github.monthalcantara.acme.infrastructure.web.dto.request.SolicitacaoRequest;
import io.github.monthalcantara.acme.infrastructure.web.dto.response.SolicitacaoResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CriaSolicitacaoControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private OrderEventProducer orderEventProducer;

    @MockBean
    private FraudClient fraudClient;

    @Test
    @DisplayName("deve criar uma solicitacao com sucesso")
    void deveCriarUmaSolicitacaoComSucesso() {
        doNothing().when(orderEventProducer).send(any());

        var fakeResponse = new FraudCheckResponse();
        when(fraudClient.checkFraud(any())).thenReturn(fakeResponse);

        var request = new SolicitacaoRequest(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "AUTO",
                "MOBILE",
                "CARTAO_CREDITO",
                BigDecimal.valueOf(150.75),
                BigDecimal.valueOf(200000),
                Map.of("Roubo", BigDecimal.valueOf(100000)),
                List.of("Guincho 24h")
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-idempotency-key", UUID.randomUUID().toString());

        var entity = new HttpEntity<>(request, headers);
        var response = restTemplate.exchange("/v1/propostas", HttpMethod.POST, entity, SolicitacaoResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertNotNull(response.getBody().getCriadoEm());
    }
}
