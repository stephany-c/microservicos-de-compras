package com.shopflow.domain.category.dto;

import com.shopflow.domain.category.entity.CategoryEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponseDTO implements java.io.Serializable {
    private String id;
    private String name;

    public CategoryResponseDTO(CategoryEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
    }
}
