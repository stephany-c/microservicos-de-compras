package com.shopflow.domain.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    public Product createProduct(Product product) {
        // Aqui entrariam as regras de negócio reais, ex:
        if (product.getPreco() == null || product.getPreco().doubleValue() <= 0) {
            throw new IllegalArgumentException("O preço do produto deve ser maior que zero.");
        }
        return repository.save(product);
    }

    public Optional<Product> getProductById(UUID id) {
        return repository.findById(id);
    }
}
