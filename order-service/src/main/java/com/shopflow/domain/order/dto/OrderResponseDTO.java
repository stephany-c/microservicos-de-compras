package com.shopflow.domain.order.dto;

import java.util.UUID;

public record OrderResponseDTO(
    UUID id,
    UUID userId,
    String productId,
    String status
) {}
