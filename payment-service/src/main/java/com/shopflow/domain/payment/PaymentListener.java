package com.shopflow.domain.payment;

import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import java.time.Instant;

@Component
public class PaymentListener {

    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private ProcessedEventRepository processedEventRepository;

    @SqsListener("payment-queue")
    public void processPaymentEvent(OrderCreatedEventDTO event) {
        String eventId = "payment-order-" + event.orderId();
        
        // 1. Checa a Idempotência no DynamoDB
        if (processedEventRepository.existsById(eventId)) {
            System.out.println("Evento já processado anteriormente. Ignorando pedido: " + event.orderId());
            return;
        }

        System.out.println("Recebido evento de pagamento via SQS para o pedido: " + event.orderId() + " no valor de " + event.amount());
        
        try {
            // Simula tempo de processamento de pagamento
            Thread.sleep(2000);
            
            String status = "FAILED";
            if (event.amount() != null && event.amount().compareTo(java.math.BigDecimal.ZERO) > 0) {
                System.out.println("Pagamento APROVADO para o pedido: " + event.orderId());
                status = "PAID";
            } else {
                System.out.println("Pagamento RECUSADO (valor inválido) para o pedido: " + event.orderId());
            }
            
            // Notifica o order-service (Simulando Webhook para não complicar a Fase 5)
            String orderUrl = "http://localhost:8082/orders/" + event.orderId() + "/status?status=" + status;
            restTemplate.put(orderUrl, null);
            
            // 2. Salva o evento como processado no DynamoDB
            processedEventRepository.save(new ProcessedEventEntity(eventId, Instant.now()));
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
