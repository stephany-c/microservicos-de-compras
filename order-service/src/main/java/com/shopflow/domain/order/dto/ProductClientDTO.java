package com.shopflow.domain.order.dto;

import java.util.UUID;
import java.math.BigDecimal;

public record ProductClientDTO(
    String id,
    String name,
    BigDecimal preco
) {}
