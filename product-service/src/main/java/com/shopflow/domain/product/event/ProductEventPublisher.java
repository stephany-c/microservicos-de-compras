package com.shopflow.domain.product.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Component
public class ProductEventPublisher {

    private static final String TOPIC_PRODUCT_EVENTS = "product-events";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void publishProductCreatedEvent(String productId) {
        String message = String.format("{\"eventType\": \"PRODUCT_CREATED\", \"productId\": \"%s\"}", productId);
        System.out.println("[KAFKA] Publicando evento: " + message);
        kafkaTemplate.send(TOPIC_PRODUCT_EVENTS, productId, message);
    }

    public void publishProductUpdatedEvent(String productId) {
        String message = String.format("{\"eventType\": \"PRODUCT_UPDATED\", \"productId\": \"%s\"}", productId);
        System.out.println("[KAFKA] Publicando evento: " + message);
        kafkaTemplate.send(TOPIC_PRODUCT_EVENTS, productId, message);
    }

    public void publishProductDeletedEvent(String productId) {
        String message = String.format("{\"eventType\": \"PRODUCT_DELETED\", \"productId\": \"%s\"}", productId);
        System.out.println("[KAFKA] Publicando evento: " + message);
        kafkaTemplate.send(TOPIC_PRODUCT_EVENTS, productId, message);
    }
}
