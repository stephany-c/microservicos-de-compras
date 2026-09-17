package com.shopflow.inventory;

public record ProductCreatedEventDTO(
    String eventType,
    String productId,
    Integer quantity
) {}
