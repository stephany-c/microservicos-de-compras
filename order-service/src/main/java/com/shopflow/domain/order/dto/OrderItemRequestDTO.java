package com.shopflow.domain.order.dto;

import jakarta.validation.constraints.NotNull;

public record OrderItemRequestDTO(
    @NotNull(message = "O ID do produto é obrigatório")
    String productId,
    Integer quantity
) {}
