package io.github.monthalcantara.acme.infrastructure.kafka.consumer;

import io.github.monthalcantara.acme.domain.service.AtualizaStatusViaEventosService;
import io.github.monthalcantara.acme.infrastructure.kafka.event.PaymentEvent;
import io.github.monthalcantara.acme.infrastructure.kafka.event.SubscriptionEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderEventConsumer {

    private final AtualizaStatusViaEventosService atualizaStatusViaEventosService;

    public OrderEventConsumer(AtualizaStatusViaEventosService atualizaStatusViaEventosService) {
        this.atualizaStatusViaEventosService = atualizaStatusViaEventosService;
    }


    @KafkaListener(topics = "payments-events", groupId = "order-service-group", containerFactory = "paymentKafkaListenerContainerFactory")
    public void consumePaymentEvent(PaymentEvent event) {
        log.info("[Consumer] Recebido evento de pagamento: orderId={}, status={}", event.getOrderId(), event.getStatus());
        atualizaStatusViaEventosService.atualizaStatusMediantePagamento(event.getOrderId(), event.getStatus());
    }


    @KafkaListener(topics = "insurance-subscriptions-events", groupId = "order-service-group", containerFactory = "subscriptionKafkaListenerContainerFactory")
    public void consumeSubscriptionEvent(SubscriptionEvent event) {
        log.info("[Consumer] Recebido evento de assinatura de seguro: orderId={}, status={}", event.getOrderId(), event.getStatus());
        atualizaStatusViaEventosService.atualizaStatusMedianteSubscricao(event.getOrderId(), event.getStatus());
    }
}