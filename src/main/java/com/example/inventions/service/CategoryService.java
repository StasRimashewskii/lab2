package com.example.inventions.service;

import com.example.inventions.dto.CategoryDto;
import com.example.inventions.entity.Category;
import com.example.inventions.entity.Invention;
import com.example.inventions.mapper.CategoryMapper;
import com.example.inventions.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Transactional
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        for (Invention invention : category.getInventions()) {
            invention.getCategories().remove(category);
        }
        categoryRepository.delete(category);
    }

    @Transactional
    public CategoryDto createCategory(CategoryDto categoryDto) {
        if (categoryRepository.findByNameIgnoreCase(categoryDto.getName()).isPresent()) {
            throw new IllegalArgumentException("Category with name '"
                    + categoryDto.getName() + "' already exists");
        }

        Category category = new Category();
        category.setName(categoryDto.getName());
        category = categoryRepository.save(category);

        return categoryMapper.convertToDto(category);
    }

    @Transactional
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        category.setName(categoryDto.getName());
        category = categoryRepository.save(category);

        return categoryMapper.convertToDto(category);
    }
}