package com.shopflow.inventory;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class ProductKafkaListener {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "product-events", groupId = "inventory-group")
    public void listen(String message) {
        try {
            System.out.println("[KAFKA INVENTORY] Recebido: " + message);
            ProductCreatedEventDTO event = objectMapper.readValue(message, ProductCreatedEventDTO.class);

            if ("PRODUCT_CREATED".equals(event.eventType())) {
                System.out.println("[KAFKA INVENTORY] Inserindo estoque inicial para o produto: " + event.productId());
                
                // Evita duplicidade simples caso a mensagem seja reprocessada
                if (inventoryRepository.findByProductId(event.productId()).isEmpty()) {
                    InventoryEntity inventory = new InventoryEntity();
                    inventory.setId(UUID.randomUUID());
                    inventory.setProductId(event.productId());
                    inventory.setAvailableQuantity(event.quantity() != null ? event.quantity() : 0);
                    inventory.setCreatedAt(Instant.now());
                    inventory.setUpdatedAt(Instant.now());
                    inventoryRepository.save(inventory);
                    System.out.println("[KAFKA INVENTORY] Estoque inserido com sucesso!");
                } else {
                    System.out.println("[KAFKA INVENTORY] Produto já existe no estoque.");
                }
            }
        } catch (Exception e) {
            System.err.println("[KAFKA INVENTORY] Erro ao processar mensagem do Kafka: " + e.getMessage());
        }
    }
}
