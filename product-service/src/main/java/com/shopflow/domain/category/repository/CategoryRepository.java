package com.shopflow.domain.category.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.shopflow.domain.category.entity.CategoryEntity;
import java.util.Optional;

public interface CategoryRepository extends MongoRepository<CategoryEntity, String> {
    Optional<CategoryEntity> findByName(String name);
}
