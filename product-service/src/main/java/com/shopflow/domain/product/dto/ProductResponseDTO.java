package com.shopflow.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.UUID;
import com.shopflow.domain.product.entity.ProductEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDTO implements java.io.Serializable {
    private String id;
    private String name;
    private String description;

    private com.shopflow.domain.product.entity.ProductStatus status;
    private BigDecimal preco;
    private Integer quantidade;
    private String type;
    private java.util.Map<String, Object> specifications;

    public ProductResponseDTO(ProductEntity product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();

        this.status = product.getStatus();
        this.preco = product.getPreco();
        this.quantidade = product.getQuantidade();
        this.type = product.getType();
        this.specifications = product.getSpecifications();
    }
}
