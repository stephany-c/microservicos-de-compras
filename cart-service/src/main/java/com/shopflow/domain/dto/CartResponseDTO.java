package com.shopflow.domain.dto;

import com.shopflow.domain.CartEntity;
import com.shopflow.domain.CartItemEntity;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record CartResponseDTO(
    UUID id,
    UUID userId,
    String status,
    List<CartItemResponseDTO> items
) {
    public CartResponseDTO(CartEntity cart) {
        this(
            cart.getId(),
            cart.getUserId(),
            cart.getStatus().name(),
            cart.getItems().stream().map(CartItemResponseDTO::new).collect(Collectors.toList())
        );
    }
}

record CartItemResponseDTO(
    UUID productId,
    Integer quantity
) {
    public CartItemResponseDTO(CartItemEntity item) {
        this(item.getProductId(), item.getQuantity());
    }
}
