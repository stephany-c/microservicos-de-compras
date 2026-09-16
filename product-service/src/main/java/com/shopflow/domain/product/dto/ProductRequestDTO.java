package com.shopflow.domain.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDTO {

    @NotBlank(message = "O nome do produto é obrigatório")
    private String name;

    private String description;



    @NotNull(message = "O status do produto é obrigatório")
    private com.shopflow.domain.product.entity.ProductStatus status;

    @NotNull(message = "O preço é obrigatório")
    @PositiveOrZero(message = "O preço deve ser maior ou igual a zero")
    private BigDecimal preco;

    @NotNull(message = "A quantidade é obrigatória")
    @PositiveOrZero(message = "A quantidade deve ser maior ou igual a zero")
    private Integer quantidade;

    @NotBlank(message = "O tipo do produto é obrigatório")
    private String type;

    private java.util.Map<String, Object> specifications;
}
