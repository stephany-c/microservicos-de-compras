package com.shopflow.domain.product.controller;

import com.shopflow.domain.product.dto.ProductRequestDTO;
import com.shopflow.domain.product.dto.ProductResponseDTO;
import com.shopflow.domain.product.service.ProductService;
import com.shopflow.domain.product.entity.ProductStatus;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService service;

    // --- CREATE ---

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO requestDTO) {
        ProductResponseDTO savedProduct = service.createProduct(requestDTO);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    // --- READ ---

    @GetMapping
    public ResponseEntity<Page<ProductResponseDTO>> getAllProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) ProductStatus status,
            Pageable pageable) {
        return ResponseEntity.ok(service.getAllProducts(name, status, pageable));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable String id) {
        return ResponseEntity.ok(service.getProductById(id));
    }

    // --- UPDATE ---

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable String id, @Valid @RequestBody ProductRequestDTO requestDTO) {
        ProductResponseDTO updatedProduct = service.updateProduct(id, requestDTO);
        return ResponseEntity.ok(updatedProduct);
    }

    // --- DELETE ---

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        service.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
