package com.shopflow.inventory;

import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.stereotype.Component;

@Component
public class InventoryListener {

    private final InventoryService inventoryService;

    public InventoryListener(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @SqsListener("inventory-queue")
    public void processInventoryEvent(OrderCreatedEventDTO event) {
        System.out.println("Recebido evento no INVENTORY para o pedido: " + event.orderId());
        
        try {
            inventoryService.processInventoryForOrder(event);
            System.out.println("Estoque reservado com sucesso para o pedido: " + event.orderId());
        } catch (Exception e) {
            System.err.println("Erro ao reservar estoque: " + e.getMessage());
        }
    }
}
