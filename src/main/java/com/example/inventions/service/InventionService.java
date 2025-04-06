package com.example.inventions.service;

import com.example.inventions.cache.InventionCache;
import com.example.inventions.dto.InventionDto;
import com.example.inventions.dto.InventionFullDto;
import com.example.inventions.entity.Category;
import com.example.inventions.entity.Invention;
import com.example.inventions.mapper.InventionMapper;
import com.example.inventions.repository.CategoryRepository;
import com.example.inventions.repository.InventionRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventionService {

    private static final String INVENTION_NOT_FOUND = "Invention not found";

    private final InventionRepository inventionRepository;
    private final CategoryRepository categoryRepository;
    private final InventionMapper inventionMapper;
    private final InventionCache inventionCache;

    public InventionService(InventionRepository inventionRepository,
                            CategoryRepository categoryRepository,
                            InventionMapper inventionMapper,
                            InventionCache inventionCache) {
        this.inventionRepository = inventionRepository;
        this.categoryRepository = categoryRepository;
        this.inventionMapper = inventionMapper;
        this.inventionCache = inventionCache;
    }

    @Transactional(readOnly = true)
    public List<InventionFullDto> getAllInventions() {
        return inventionRepository.findAll().stream()
                .map(inventionMapper::convertToFullDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public InventionDto getInventionById(Long id) {
        Invention invention = inventionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(INVENTION_NOT_FOUND));
        return inventionMapper.convertToDto(invention);
    }

    @Transactional(readOnly = true)
    public List<InventionDto> findInventionsByTitle(String title) {
        String cacheKey = "inventions_by_title:" + title.toLowerCase();

        List<InventionDto> cachedResult = inventionCache.getList(cacheKey, InventionDto.class);
        if (cachedResult != null) {
            return cachedResult;
        }

        List<Invention> inventions = inventionRepository.findByTitleContainingIgnoreCase(title);
        if (inventions.isEmpty()) {
            throw new EntityNotFoundException("No inventions found with title: " + title);
        }

        List<InventionDto> result = inventions.stream()
                .map(inventionMapper::convertToDto)
                .toList();

        inventionCache.put(cacheKey, result);
        return result;
    }

    @Transactional(readOnly = true)
    public List<InventionDto> findInventionsByTitleNative(String title) {
        String cacheKey = "inventions_by_title_native:" + title.toLowerCase();

        List<InventionDto> cachedResult = inventionCache.getList(cacheKey, InventionDto.class);
        if (cachedResult != null) {
            return cachedResult;
        }

        List<Invention> inventions = inventionRepository.findByTitleContainingIgnoreCaseNative(title);
        if (inventions.isEmpty()) {
            throw new EntityNotFoundException("No inventions found with title: " + title);
        }

        List<InventionDto> result = inventions.stream()
                .map(inventionMapper::convertToDto)
                .toList();

        inventionCache.put(cacheKey, result);
        return result;
    }

    @Transactional
    public InventionFullDto createInvention(InventionDto inventionDto) {
        Invention invention = new Invention(
                inventionDto.getTitle(),
                inventionDto.getDescription(),
                inventionDto.getInstruction()
        );

        Set<Category> categories = inventionDto.getCategories().stream()
                .map(categoryDto -> categoryRepository.findByNameIgnoreCase(categoryDto.getName())
                        .orElseGet(() -> {
                            Category newCategory = new Category();
                            newCategory.setName(categoryDto.getName());
                            return categoryRepository.save(newCategory);
                        }))
                .collect(Collectors.toSet());

        invention.setCategories(categories);
        invention = inventionRepository.save(invention);

        return inventionMapper.convertToFullDto(invention);
    }

    @Transactional
    public InventionFullDto updateInvention(Long inventionId, InventionDto inventionDto) {
        Invention invention = inventionRepository.findById(inventionId)
                .orElseThrow(() -> new EntityNotFoundException(INVENTION_NOT_FOUND));

        invention.setTitle(inventionDto.getTitle());
        invention.setDescription(inventionDto.getDescription());
        invention.setInstruction(inventionDto.getInstruction());
        invention = inventionRepository.save(invention);

        return inventionMapper.convertToFullDto(invention);
    }

    @Transactional
    public void deleteInventionById(Long id) {
        Invention invention = inventionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(INVENTION_NOT_FOUND));

        invention.getCategories().clear();
        inventionRepository.save(invention);

        inventionRepository.delete(invention);
    }

    // InventionService.java
    public void removeCategoryFromInvention(Long inventionId, Long categoryId) {
        Invention invention = inventionRepository.findById(inventionId)
                .orElseThrow(() -> new EntityNotFoundException("Invention not found"));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        invention.getCategories().remove(category);
        inventionRepository.save(invention);
    }

    @Transactional(readOnly = true)
    public List<InventionDto> findInventionsByCategoryNames(List<String> categoryNames) {
        List<Invention> inventions = inventionRepository.findByCategoriesNameIn(
                categoryNames.stream()
                        .map(String::toLowerCase)
                        .collect(Collectors.toList())
        );

        if (inventions.isEmpty()) {
            throw new EntityNotFoundException("No inventions found with categories: " + categoryNames);
        }

        return inventions.stream()
                .map(inventionMapper::convertToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<InventionDto> findInventionsByAuthorsCountry(String country) {
        List<Invention> inventions = inventionRepository.findByInventorsCountry(country);

        if (inventions.isEmpty()) {
            throw new EntityNotFoundException("No inventions found with authors from country: " + country);
        }

        return inventions.stream()
                .map(inventionMapper::convertToDto)
                .toList();
    }


    @Transactional(readOnly = true)
    public List<InventionDto> findInventionsByExactCategories(List<String> categoryNames) {
        List<Invention> inventions = inventionRepository.findByExactCategories(
                categoryNames.stream().map(String::toLowerCase).collect(Collectors.toList()),
                categoryNames.size()
        );

        if (inventions.isEmpty()) {
            throw new EntityNotFoundException("No inventions found with exact categories: " + categoryNames);
        }

        return inventions.stream()
                .map(inventionMapper::convertToDto)
                .toList();
    }

}