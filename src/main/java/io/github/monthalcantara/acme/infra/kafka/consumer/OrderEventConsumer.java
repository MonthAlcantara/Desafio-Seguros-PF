package io.github.monthalcantara.acme.infra.kafka.consumer;

import io.github.monthalcantara.acme.application.service.OrderService;
import io.github.monthalcantara.acme.infra.kafka.event.PaymentEvent;
import io.github.monthalcantara.acme.infra.kafka.event.SubscriptionEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderEventConsumer {

    private final OrderService orderService;

    public OrderEventConsumer(OrderService orderService) {
        this.orderService = orderService;
    }


    @KafkaListener(topics = "payments-events", groupId = "order-service-group",
            containerFactory = "paymentKafkaListenerContainerFactory")
    public void consumePaymentEvent(PaymentEvent event) {
        log.info("[Consumer] Recebido evento de pagamento: orderId={}, status={}", event.getOrderId(), event.getStatus());
        orderService.updateStatusFromPayment(event.getOrderId(), event.getStatus());
    }


    @KafkaListener(topics = "insurance-subscriptions-events", groupId = "order-service-group",
            containerFactory = "subscriptionKafkaListenerContainerFactory")
    public void consumeSubscriptionEvent(SubscriptionEvent event) {
        log.info("[Consumer] Recebido evento de assinatura de seguro: orderId={}, status={}", event.getOrderId(), event.getStatus());
        orderService.updateStatusFromSubscription(event.getOrderId(), event.getStatus());
    }
}