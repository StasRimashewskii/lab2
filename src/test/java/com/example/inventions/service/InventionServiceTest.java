package com.example.inventions.service;

import com.example.inventions.cache.InventionCache;
import com.example.inventions.dto.CategoryDto;
import com.example.inventions.dto.InventionDto;
import com.example.inventions.dto.InventionFullDto;
import com.example.inventions.entity.Category;
import com.example.inventions.entity.Invention;
import com.example.inventions.mapper.InventionMapper;
import com.example.inventions.repository.CategoryRepository;
import com.example.inventions.repository.InventionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventionServiceTest {

    @Mock
    private InventionRepository inventionRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private InventionMapper inventionMapper;

    @Mock
    private InventionCache inventionCache;

    @InjectMocks
    private InventionService inventionService;

    @Test
    void getAllInventions() {
        Invention invention = new Invention();
        when(inventionRepository.findAll()).thenReturn(List.of(invention));
        InventionFullDto inventionFullDto = new InventionFullDto();
        when(inventionMapper.convertToFullDto(invention)).thenReturn(inventionFullDto);

        List<InventionFullDto> result = inventionService.getAllInventions();

        assertEquals(1, result.size());
        assertEquals(inventionFullDto, result.get(0));
        verify(inventionRepository).findAll();
        verify(inventionMapper).convertToFullDto(invention);
    }

    @Test
    void getInventionById() {
        Long id = 1L;
        Invention invention = new Invention();
        when(inventionRepository.findById(id)).thenReturn(Optional.of(invention));
        InventionDto inventionDto = new InventionDto();
        when(inventionMapper.convertToDto(invention)).thenReturn(inventionDto);

        InventionDto result = inventionService.getInventionById(id);

        assertEquals(inventionDto, result);
        verify(inventionRepository).findById(id);
        verify(inventionMapper).convertToDto(invention);
    }

    @Test
    void getInventionById_NotFound() {
        Long id = 1L;
        when(inventionRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> inventionService.getInventionById(id));

        assertEquals("Invention not found", exception.getMessage());
        verify(inventionRepository).findById(id);
    }

    @Test
    void findInventionsByTitle() {
        String title = "test";
        String cacheKey = "inventions_by_title:test";

        when(inventionCache.getList(cacheKey, InventionDto.class)).thenReturn(null);
        Invention invention = new Invention();
        when(inventionRepository.findByTitleContainingIgnoreCase(title)).thenReturn(List.of(invention));
        InventionDto inventionDto = new InventionDto();
        when(inventionMapper.convertToDto(invention)).thenReturn(inventionDto);

        List<InventionDto> result = inventionService.findInventionsByTitle(title);

        assertEquals(1, result.size());
        assertEquals(inventionDto, result.get(0));
        verify(inventionCache).getList(cacheKey, InventionDto.class);
        verify(inventionRepository).findByTitleContainingIgnoreCase(title);
        verify(inventionMapper).convertToDto(invention);
        verify(inventionCache).put(cacheKey, result);
    }

    @Test
    void findInventionsByTitle_NoResults() {
        String title = "test";
        when(inventionCache.getList(anyString(), eq(InventionDto.class))).thenReturn(null);
        when(inventionRepository.findByTitleContainingIgnoreCase(title)).thenReturn(Collections.emptyList());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> inventionService.findInventionsByTitle(title));

        assertEquals("No inventions found with title: test", exception.getMessage());
        verify(inventionRepository).findByTitleContainingIgnoreCase(title);
    }

    @Test
    void createInvention() {
        InventionDto inventionDto = new InventionDto();
        inventionDto.setTitle("Test");
        inventionDto.setDescription("Description");
        inventionDto.setInstruction("Instruction");

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("Category1");
        inventionDto.setCategories(Set.of(categoryDto));

        Category category = new Category();
        category.setName("Category1");
        when(categoryRepository.findByNameIgnoreCase("Category1")).thenReturn(Optional.empty());
        when(categoryRepository.save(any())).thenReturn(category);

        Invention invention = new Invention("Test", "Description", "Instruction");
        when(inventionRepository.save(any())).thenReturn(invention);

        InventionFullDto inventionFullDto = new InventionFullDto();
        when(inventionMapper.convertToFullDto(invention)).thenReturn(inventionFullDto);

        InventionFullDto result = inventionService.createInvention(inventionDto);

        assertEquals(inventionFullDto, result);
        verify(categoryRepository).findByNameIgnoreCase("Category1");
        verify(categoryRepository).save(any());
        verify(inventionRepository).save(any());
        verify(inventionMapper).convertToFullDto(invention);
    }

    @Test
    void updateInvention() {
        Long inventionId = 1L;
        InventionDto inventionDto = new InventionDto();
        inventionDto.setTitle("Updated Title");
        inventionDto.setDescription("Updated Description");
        inventionDto.setInstruction("Updated Instruction");

        Invention invention = new Invention();
        when(inventionRepository.findById(inventionId)).thenReturn(Optional.of(invention));
        when(inventionRepository.save(invention)).thenReturn(invention);

        InventionFullDto inventionFullDto = new InventionFullDto();
        when(inventionMapper.convertToFullDto(invention)).thenReturn(inventionFullDto);

        InventionFullDto result = inventionService.updateInvention(inventionId, inventionDto);

        assertEquals(inventionFullDto, result);
        verify(inventionRepository).findById(inventionId);
        verify(inventionRepository).save(invention);
        verify(inventionMapper).convertToFullDto(invention);
    }

    @Test
    void deleteInventionById() {
        Long id = 1L;

        // Создаем фиктивный объект Invention с изменяемым множеством категорий
        Invention invention = new Invention();
        invention.setTitle("Test Title");
        invention.setCategories(new HashSet<>()); // Изменяемое множество

        when(inventionRepository.findById(id)).thenReturn(Optional.of(invention));

        inventionService.deleteInventionById(id);

        // Проверяем, что методы репозитория и кеша вызваны
        verify(inventionRepository).findById(id);
        verify(inventionRepository).save(invention); // Для очистки категорий
        verify(inventionRepository).delete(invention);
        verify(inventionCache, times(2)).evictByKeyPrefix(anyString());
    }

    @Test
    void findInventionsByCategoryNames() {
        List<String> categoryNames = List.of("Category1", "Category2");
        Invention invention = new Invention();
        when(inventionRepository.findByCategoriesNameIn(anyList())).thenReturn(List.of(invention));
        InventionDto inventionDto = new InventionDto();
        when(inventionMapper.convertToDto(invention)).thenReturn(inventionDto);

        List<InventionDto> result = inventionService.findInventionsByCategoryNames(categoryNames);

        assertEquals(1, result.size());
        assertEquals(inventionDto, result.get(0));
        verify(inventionRepository).findByCategoriesNameIn(anyList());
        verify(inventionMapper).convertToDto(invention);
    }

    @Test
    void findInventionsByAuthorCountry() {
        String country = "USA";
        Invention invention = new Invention();
        when(inventionRepository.findByAuthorCountry(country)).thenReturn(List.of(invention));
        InventionDto inventionDto = new InventionDto();
        when(inventionMapper.convertToDto(invention)).thenReturn(inventionDto);

        List<InventionDto> result = inventionService.findInventionsByAuthorCountry(country);

        assertEquals(1, result.size());
        assertEquals(inventionDto, result.get(0));
        verify(inventionRepository).findByAuthorCountry(country);
        verify(inventionMapper).convertToDto(invention);
    }

    @Test
    void findInventionsByTitleNative() {
        String title = "native test";
        String cacheKey = "inventions_by_title_native:native test";

        when(inventionCache.getList(cacheKey, InventionDto.class)).thenReturn(null);
        Invention invention = new Invention();
        when(inventionRepository.findByTitleContainingIgnoreCaseNative(title)).thenReturn(List.of(invention));
        InventionDto inventionDto = new InventionDto();
        when(inventionMapper.convertToDto(invention)).thenReturn(inventionDto);

        List<InventionDto> result = inventionService.findInventionsByTitleNative(title);

        assertEquals(1, result.size());
        assertEquals(inventionDto, result.get(0));
        verify(inventionCache).getList(cacheKey, InventionDto.class);
        verify(inventionRepository).findByTitleContainingIgnoreCaseNative(title);
        verify(inventionMapper).convertToDto(invention);
        verify(inventionCache).put(cacheKey, result);
    }

    @Test
    void removeCategoryFromInvention() {
        Long inventionId = 1L;
        Long categoryId = 1L;

        Invention invention = new Invention();
        Set<Category> categories = new HashSet<>();
        Category category = new Category();
        category.setId(categoryId);
        categories.add(category);
        invention.setCategories(categories);

        when(inventionRepository.findById(inventionId)).thenReturn(Optional.of(invention));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        inventionService.removeCategoryFromInvention(inventionId, categoryId);

        assertFalse(invention.getCategories().contains(category));
        verify(inventionRepository).findById(inventionId);
        verify(categoryRepository).findById(categoryId);
        verify(inventionRepository).save(invention);
    }

    @Test
    void findInventionsByExactCategories() {
        List<String> categoryNames = List.of("category1", "category2"); // Corrected argument
        Invention invention = new Invention();
        when(inventionRepository.findByExactCategories(categoryNames, categoryNames.size())).thenReturn(List.of(invention)); // Make sure to use the same argument as in the code

        InventionDto inventionDto = new InventionDto();
        when(inventionMapper.convertToDto(invention)).thenReturn(inventionDto);

        List<InventionDto> result = inventionService.findInventionsByExactCategories(categoryNames);

        assertEquals(1, result.size());
        assertEquals(inventionDto, result.get(0));
        verify(inventionRepository).findByExactCategories(categoryNames, categoryNames.size());
        verify(inventionMapper).convertToDto(invention);
    }


    @Test
    void findInventionsByCategoryNames_NoResults() {
        List<String> categoryNames = List.of("category1", "category2"); // Corrected argument
        when(inventionRepository.findByCategoriesNameIn(categoryNames)).thenReturn(Collections.emptyList());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> inventionService.findInventionsByCategoryNames(categoryNames));

        assertEquals("No inventions found with categories: [category1, category2]", exception.getMessage()); // Use lowercase category names
        verify(inventionRepository).findByCategoriesNameIn(categoryNames);
    }


    @Test
    void findInventionsByAuthorCountry_NoResults() {
        String country = "Russia";

        when(inventionRepository.findByAuthorCountry(country)).thenReturn(Collections.emptyList());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> inventionService.findInventionsByAuthorCountry(country));

        assertEquals("No inventions found with authors from country: Russia", exception.getMessage());
        verify(inventionRepository).findByAuthorCountry(country);
    }

    @Test
    void deleteInventionById_AdditionalCacheVerification() {
        Long id = 2L;
        Invention invention = new Invention();
        invention.setTitle("Title to be deleted");

        when(inventionRepository.findById(id)).thenReturn(Optional.of(invention));

        inventionService.deleteInventionById(id);

        verify(inventionCache, times(2)).evictByKeyPrefix(anyString()); // Проверяем, что оба ключа кеша были удалены
    }

    @Test
    void createInvention_CategorySaveFails() {
        InventionDto inventionDto = new InventionDto();
        inventionDto.setTitle("Test");
        inventionDto.setDescription("Description");
        inventionDto.setInstruction("Instruction");

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("Category1");
        inventionDto.setCategories(Set.of(categoryDto));

        Category category = new Category();
        category.setName("Category1");
        when(categoryRepository.findByNameIgnoreCase("Category1")).thenReturn(Optional.empty());
        when(categoryRepository.save(any())).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> inventionService.createInvention(inventionDto));

        assertEquals("Database error", exception.getMessage());
        verify(categoryRepository).findByNameIgnoreCase("Category1");
        verify(categoryRepository).save(any());
    }

}