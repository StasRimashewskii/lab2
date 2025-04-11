package com.example.inventions.mapper;

import com.example.inventions.dto.InventionDto;
import com.example.inventions.dto.InventionFullDto;
import com.example.inventions.entity.Invention;
import org.springframework.stereotype.Component;

@Component
public class InventionMapper {

    private final CategoryMapper categoryMapper;
    private final AuthorMapper authorMapper;

    public InventionMapper(CategoryMapper categoryMapper, AuthorMapper authorMapper) {
        this.categoryMapper = categoryMapper;
        this.authorMapper = authorMapper;
    }

    public InventionFullDto convertToFullDto(Invention invention) {
        InventionFullDto dto = new InventionFullDto();
        dto.setId(invention.getId());
        dto.setTitle(invention.getTitle());
        dto.setDescription(invention.getDescription());
        dto.setInstruction(invention.getInstruction());
        dto.setCategories(categoryMapper.convertToDto(invention.getCategories()));
        dto.setAuthors(authorMapper.convertToDto(invention.getAuthors())); // Изменено на authors
        return dto;
    }

    public InventionDto convertToDto(Invention invention) {
        InventionDto dto = new InventionDto();
        dto.setTitle(invention.getTitle());
        dto.setDescription(invention.getDescription());
        dto.setInstruction(invention.getInstruction());
        dto.setCategories(categoryMapper.convertToDto(invention.getCategories()));
        return dto;
    }
}
