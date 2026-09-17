package com.shopflow.domain.payment;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderCreatedEventDTO(
        UUID orderId,
        String productId,
        BigDecimal amount
) {}
