package com.shopflow.domain.category.service;

import com.shopflow.domain.category.entity.CategoryEntity;
import com.shopflow.domain.category.repository.CategoryRepository;
import com.shopflow.domain.category.dto.CategoryRequestDTO;
import com.shopflow.domain.category.dto.CategoryResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    public CategoryResponseDTO createCategory(CategoryRequestDTO requestDTO) {
        if (repository.findByName(requestDTO.getName()).isPresent()) {
            throw new RuntimeException("Category already exists with name: " + requestDTO.getName());
        }

        CategoryEntity entity = new CategoryEntity();
        entity.setName(requestDTO.getName());
        CategoryEntity saved = repository.save(entity);
        return new CategoryResponseDTO(saved);
    }

    public List<CategoryResponseDTO> getAllCategories() {
        return repository.findAll().stream()
                .map(CategoryResponseDTO::new)
                .collect(Collectors.toList());
    }
}
