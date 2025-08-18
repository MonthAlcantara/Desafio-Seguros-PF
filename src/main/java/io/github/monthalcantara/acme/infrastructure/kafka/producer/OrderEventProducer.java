package io.github.monthalcantara.acme.infrastructure.kafka.producer;

import io.github.monthalcantara.acme.infrastructure.kafka.event.OrderStatusEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderEventProducer {
    private static final String TOPIC_ORDER_STATUS = "order-status-events";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(OrderStatusEvent event) {
        log.info("[OrderEventProducer] Produzindo evento para {}: {}", TOPIC_ORDER_STATUS, event);
        this.kafkaTemplate.send(TOPIC_ORDER_STATUS, event.getOrderId().toString(), event);
    }
}