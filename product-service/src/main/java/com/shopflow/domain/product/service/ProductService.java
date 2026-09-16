package com.shopflow.domain.product.service;


import com.shopflow.domain.product.dto.ProductRequestDTO;
import com.shopflow.domain.product.dto.ProductResponseDTO;
import com.shopflow.domain.product.entity.ProductEntity;
import com.shopflow.domain.product.entity.ProductStatus;
import com.shopflow.domain.product.repository.ProductRepository;
import com.shopflow.domain.product.event.ProductEventPublisher;
import com.shopflow.domain.product.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;



import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;



    @Autowired
    private ProductEventPublisher eventPublisher;

    @Cacheable(value = "products")
    public Page<ProductResponseDTO> getAllProducts(String name, ProductStatus status, Pageable pageable) {
        Page<ProductEntity> products;
        if (name != null && status != null) {
            products = repository.findByNameContainingIgnoreCaseAndStatus(name, status, pageable);
        } else if (name != null) {
            products = repository.findByNameContainingIgnoreCase(name, pageable);
        } else if (status != null) {
            products = repository.findByStatus(status, pageable);
        } else {
            products = repository.findAll(pageable);
        }
        return products.map(ProductResponseDTO::new);
    }

    @CacheEvict(value = {"products", "product"}, allEntries = true)
    public ProductResponseDTO createProduct(ProductRequestDTO requestDTO) {
        ProductEntity product = ProductEntity.builder()
                .name(requestDTO.getName())
                .description(requestDTO.getDescription())
                .status(requestDTO.getStatus())
                .preco(requestDTO.getPreco())
                .quantidade(requestDTO.getQuantidade())
                .type(requestDTO.getType())
                .specifications(requestDTO.getSpecifications())
                .build();
        ProductEntity savedProduct = repository.save(product);
        eventPublisher.publishProductCreatedEvent(savedProduct.getId());
        return new ProductResponseDTO(savedProduct);
    }

    @Cacheable(value = "product", key = "#id")
    public ProductResponseDTO getProductById(String id) {
        ProductEntity product = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("ProductEntity not found with id: " + id));
        return new ProductResponseDTO(product);
    }

    @CacheEvict(value = {"products", "product"}, allEntries = true)
    public ProductResponseDTO updateProduct(String id, ProductRequestDTO requestDTO) {
        ProductEntity existingProduct = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("ProductEntity not found with id: " + id));

        existingProduct.setName(requestDTO.getName());
        existingProduct.setDescription(requestDTO.getDescription());
        existingProduct.setStatus(requestDTO.getStatus());
        existingProduct.setPreco(requestDTO.getPreco());
        existingProduct.setQuantidade(requestDTO.getQuantidade());
        existingProduct.setType(requestDTO.getType());
        existingProduct.setSpecifications(requestDTO.getSpecifications());
        
        ProductEntity updatedProduct = repository.save(existingProduct);
        eventPublisher.publishProductUpdatedEvent(updatedProduct.getId());
        return new ProductResponseDTO(updatedProduct);
    }

    @CacheEvict(value = {"products", "product"}, allEntries = true)
    public void deleteProduct(String id) {
        if (!repository.existsById(id)) {
            throw new ProductNotFoundException("ProductEntity not found with id: " + id);
        }
        repository.deleteById(id);
        eventPublisher.publishProductDeletedEvent(id);
    }
}
