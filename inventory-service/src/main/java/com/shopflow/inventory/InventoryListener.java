package com.shopflow.inventory;

import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.stereotype.Component;

@Component
public class InventoryListener {

    @SqsListener("inventory-queue")
    public void processInventoryEvent(PaymentRequestDTO dto) {
        System.out.println("Recebido evento no INVENTORY para o pedido: " + dto.orderId());
        
        try {
            // Simula tempo de reserva de estoque
            Thread.sleep(1000);
            System.out.println("Estoque reservado com sucesso para o pedido: " + dto.orderId());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
