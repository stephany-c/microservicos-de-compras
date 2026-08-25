package com.shopflow.domain.order.dto;

import java.util.UUID;
import java.math.BigDecimal;

public record PaymentRequestDTO(
    UUID orderId,
    BigDecimal amount
) {}
