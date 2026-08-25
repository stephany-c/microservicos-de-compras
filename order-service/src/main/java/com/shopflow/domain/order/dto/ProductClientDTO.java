package com.shopflow.domain.order.dto;

import java.util.UUID;
import java.math.BigDecimal;

public record ProductClientDTO(
    UUID id,
    String name,
    BigDecimal preco
) {}
