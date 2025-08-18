package io.github.monthalcantara.acme.infrastructure.web;

import io.github.monthalcantara.acme.infrastructure.kafka.producer.OrderEventProducer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CancelaSolicitacaoControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private OrderEventProducer orderEventProducer;

    @Test
    @DisplayName("deve retornar NotFound ao cancelar solicitacao inexistente")
    void deveRetornarNotFoundAoCancelarSolicitacaoInexistente() {
        ResponseEntity<Void> response = restTemplate.exchange(
                "/v1/propostas/" + UUID.randomUUID() + "/cancelar",
                HttpMethod.PATCH,
                null,
                Void.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
