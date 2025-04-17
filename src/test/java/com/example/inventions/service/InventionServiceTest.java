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
}