package io.github.monthalcantara.acme.infrastructure.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.monthalcantara.acme.TestUtils;
import io.github.monthalcantara.acme.domain.service.AtualizaStatusViaEventosService;
import io.github.monthalcantara.acme.infrastructure.kafka.event.PaymentEvent;
import io.github.monthalcantara.acme.infrastructure.kafka.event.SubscriptionEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class OrderEventConsumerIT {

    @Autowired
    private OrderEventConsumer consumer;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AtualizaStatusViaEventosService atualizaStatusViaEventosService;

    @Test
    @DisplayName("Deve consumir evento de pagamento e chamar service")
    void deveConsumirEventoPagamento() throws Exception {
        var event = TestUtils.lerPayloadDoArquivo("/eventos/payments-events/pagamento-aprovado.json");
        PaymentEvent paymentEvent = objectMapper.readValue(event, PaymentEvent.class);

        consumer.consumePaymentEvent(paymentEvent);

        verify(atualizaStatusViaEventosService, times(1))
                .atualizaStatusMediantePagamento(paymentEvent.getOrderId(), paymentEvent.getStatus());
    }

    @Test
    @DisplayName("Deve consumir evento de subscrição e chamar service")
    void deveConsumirEventoSubscricao() throws Exception {
        var event = TestUtils.lerPayloadDoArquivo("/eventos/subscriptions-events/subscricao-aprovado.json");
        SubscriptionEvent subscriptionEvent = objectMapper.readValue(event, SubscriptionEvent.class);

        consumer.consumeSubscriptionEvent(subscriptionEvent);

        verify(atualizaStatusViaEventosService, times(1))
                .atualizaStatusMedianteSubscricao(subscriptionEvent.getOrderId(), subscriptionEvent.getStatus());
    }
}
