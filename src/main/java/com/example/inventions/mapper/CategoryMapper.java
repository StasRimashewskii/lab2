package com.example.inventions.mapper;

import com.example.inventions.dto.CategoryDto;
import com.example.inventions.entity.Category;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Set<CategoryDto> convertToDto(Set<Category> categories) {
        if (categories == null) {
            return new HashSet<>();
        }
        return categories.stream()
                .map(this::convertToDto)
                .collect(Collectors.toSet());
    }

    public CategoryDto convertToDto(Category category) {
        if (category == null) {
            return null;
        }
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        return dto;
    }
}