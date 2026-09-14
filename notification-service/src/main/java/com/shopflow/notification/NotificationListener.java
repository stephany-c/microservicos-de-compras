package com.shopflow.notification;

import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {

    @SqsListener("notification-queue")
    public void processNotificationEvent(PaymentRequestDTO dto) {
        System.out.println("Recebido evento no NOTIFICATION para o pedido: " + dto.orderId());
        
        try {
            // Simula tempo de envio de e-mail
            Thread.sleep(500);
            System.out.println("E-mail de confirmação enviado para o pedido: " + dto.orderId());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
