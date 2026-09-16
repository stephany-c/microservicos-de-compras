package com.shopflow.domain.order.dto;

import java.util.UUID;

public record OrderRequestDTO(
    UUID userId,
    String productId
) {}
