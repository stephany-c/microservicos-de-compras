package com.shopflow.domain.product.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Document(collection = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductEntity {

    @Id
    private String id;
    private String name;

    private String description;



    private ProductStatus status;

    private BigDecimal preco;

    private Integer quantidade;

    // Flexible Schema support
    private String type;

    private Map<String, Object> specifications;
}
