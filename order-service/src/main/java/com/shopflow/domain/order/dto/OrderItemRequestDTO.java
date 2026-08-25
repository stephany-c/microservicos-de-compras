package com.shopflow.domain.order.dto;

import java.util.UUID;

public record OrderItemRequestDTO(
    UUID productId,
    Integer quantity
) {}
