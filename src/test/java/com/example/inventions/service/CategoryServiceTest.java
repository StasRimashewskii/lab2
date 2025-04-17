package com.example.inventions.service;

import com.example.inventions.dto.CategoryDto;
import com.example.inventions.entity.Category;
import com.example.inventions.entity.Invention;
import com.example.inventions.mapper.CategoryMapper;
import com.example.inventions.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void deleteCategory_shouldRemoveCategoryAndItsReferencesFromInventions() {
        // Arrange
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        category.setName("Test Category");

        Invention invention1 = new Invention();
        invention1.setId(1L);
        Invention invention2 = new Invention();
        invention2.setId(2L);

        Set<Invention> inventions = new HashSet<>();
        inventions.add(invention1);
        inventions.add(invention2);
        category.setInventions(inventions);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // Act
        categoryService.deleteCategory(categoryId);

        // Assert
        assertTrue(invention1.getCategories().isEmpty());
        assertTrue(invention2.getCategories().isEmpty());
        verify(categoryRepository).delete(category);
    }

    @Test
    void deleteCategory_whenCategoryNotFound_shouldThrowEntityNotFoundException() {
        // Arrange
        Long categoryId = 1L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> categoryService.deleteCategory(categoryId));
        verify(categoryRepository, never()).delete(any());
    }

    @Test
    void createCategory_shouldSaveNewCategory() {
        // Arrange
        CategoryDto inputDto = new CategoryDto();
        inputDto.setName("New Category");

        Category savedCategory = new Category();
        savedCategory.setId(1L);
        savedCategory.setName("New Category");

        CategoryDto expectedDto = new CategoryDto();
        expectedDto.setId(1L);
        expectedDto.setName("New Category");

        when(categoryRepository.findByNameIgnoreCase("New Category")).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);
        when(categoryMapper.convertToDto(savedCategory)).thenReturn(expectedDto);

        // Act
        CategoryDto result = categoryService.createCategory(inputDto);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("New Category", result.getName());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void createCategory_whenCategoryExists_shouldThrowIllegalArgumentException() {
        // Arrange
        CategoryDto inputDto = new CategoryDto();
        inputDto.setName("Existing Category");

        Category existingCategory = new Category();
        existingCategory.setId(1L);
        existingCategory.setName("Existing Category");

        when(categoryRepository.findByNameIgnoreCase("Existing Category"))
                .thenReturn(Optional.of(existingCategory));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> categoryService.createCategory(inputDto));
        assertEquals("Category with name 'Existing Category' already exists", exception.getMessage());
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void updateCategory_shouldUpdateExistingCategory() {
        // Arrange
        Long categoryId = 1L;
        CategoryDto inputDto = new CategoryDto();
        inputDto.setName("Updated Name");

        Category existingCategory = new Category();
        existingCategory.setId(categoryId);
        existingCategory.setName("Original Name");

        Category updatedCategory = new Category();
        updatedCategory.setId(categoryId);
        updatedCategory.setName("Updated Name");

        CategoryDto expectedDto = new CategoryDto();
        expectedDto.setId(categoryId);
        expectedDto.setName("Updated Name");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);
        when(categoryMapper.convertToDto(updatedCategory)).thenReturn(expectedDto);

        // Act
        CategoryDto result = categoryService.updateCategory(categoryId, inputDto);

        // Assert
        assertNotNull(result);
        assertEquals(categoryId, result.getId());
        assertEquals("Updated Name", result.getName());
        verify(categoryRepository).save(existingCategory);
    }

    @Test
    void updateCategory_whenCategoryNotFound_shouldThrowEntityNotFoundException() {
        // Arrange
        Long categoryId = 1L;
        CategoryDto inputDto = new CategoryDto();
        inputDto.setName("Updated Name");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class,
                () -> categoryService.updateCategory(categoryId, inputDto));
        verify(categoryRepository, never()).save(any());
    }
}