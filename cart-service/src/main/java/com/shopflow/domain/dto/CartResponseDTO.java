package com.shopflow.domain.dto;

import com.shopflow.domain.Cart;
import com.shopflow.domain.CartItem;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record CartResponseDTO(
    UUID id,
    UUID userId,
    String status,
    List<CartItemResponseDTO> items
) {
    public CartResponseDTO(Cart cart) {
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
    public CartItemResponseDTO(CartItem item) {
        this(item.getProductId(), item.getQuantity());
    }
}
