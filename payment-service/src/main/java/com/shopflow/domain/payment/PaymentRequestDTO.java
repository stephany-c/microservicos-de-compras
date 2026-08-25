package com.shopflow.domain.payment;

import java.util.UUID;

public record PaymentRequestDTO(
    UUID orderId,
    Double amount
) {}
