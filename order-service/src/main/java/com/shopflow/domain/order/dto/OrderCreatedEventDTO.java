package com.shopflow.domain.order.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderCreatedEventDTO(
        UUID orderId,
        String productId,
        BigDecimal amount
) {}
