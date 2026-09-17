package com.shopflow.notification;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ProductKafkaListener {

    @KafkaListener(topics = "product-events", groupId = "notification-group")
    public void listenProductEvents(String message) {
        System.out.println("=================================================");
        System.out.println("[KAFKA CONSUMER] Novo evento de catálogo recebido!");
        System.out.println("Payload: " + message);
        System.out.println("Ação: Enviando Push Notification para usuários inscritos...");
        System.out.println("=================================================");
    }
}
