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
    public void processPaymentEvent(PaymentRequestDTO dto) {
        String eventId = "payment-order-" + dto.orderId();
        
        // 1. Checa a Idempotência no DynamoDB
        if (processedEventRepository.existsById(eventId)) {
            System.out.println("Evento já processado anteriormente. Ignorando pedido: " + dto.orderId());
            return;
        }

        System.out.println("Recebido evento de pagamento via SQS para o pedido: " + dto.orderId() + " no valor de " + dto.amount());
        
        try {
            // Simula tempo de processamento de pagamento
            Thread.sleep(2000);
            
            String status = "FAILED";
            if (dto.amount() != null && dto.amount() > 0) {
                System.out.println("Pagamento APROVADO para o pedido: " + dto.orderId());
                status = "PAID";
            } else {
                System.out.println("Pagamento RECUSADO (valor inválido) para o pedido: " + dto.orderId());
            }
            
            // Notifica o order-service (Simulando Webhook para não complicar a Fase 5)
            String orderUrl = "http://localhost:8082/orders/" + dto.orderId() + "/status?status=" + status;
            restTemplate.put(orderUrl, null);
            
            // 2. Salva o evento como processado no DynamoDB
            processedEventRepository.save(new ProcessedEventEntity(eventId, Instant.now()));
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
