package com.shopflow.inventory;

import java.util.UUID;

public record PaymentRequestDTO(UUID orderId, Double amount) {
}
