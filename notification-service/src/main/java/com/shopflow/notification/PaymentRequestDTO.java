package com.shopflow.notification;

import java.util.UUID;

public record PaymentRequestDTO(UUID orderId, Double amount) {
}
