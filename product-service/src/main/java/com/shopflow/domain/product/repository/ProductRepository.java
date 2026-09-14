package com.shopflow.domain.product.repository;

import com.shopflow.domain.product.entity.ProductEntity;
import com.shopflow.domain.product.entity.ProductStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepository extends MongoRepository<ProductEntity, String> {
    Page<ProductEntity> findByNameContainingIgnoreCaseAndStatus(String name, ProductStatus status, Pageable pageable);
    Page<ProductEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<ProductEntity> findByStatus(ProductStatus status, Pageable pageable);
}
