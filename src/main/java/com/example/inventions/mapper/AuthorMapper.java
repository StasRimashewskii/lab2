package com.example.inventions.mapper;

import com.example.inventions.dto.AuthorDto;
import com.example.inventions.dto.AuthorFullDto;
import com.example.inventions.dto.InventionDto;
import com.example.inventions.entity.Author;
import com.example.inventions.entity.Invention;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {

    private final InventionMapper inventionMapper;

    public AuthorMapper(InventionMapper inventionMapper) {
        this.inventionMapper = inventionMapper;
    }

    public AuthorDto convertToDto(Author author) {
        AuthorDto dto = new AuthorDto();
        dto.setId(author.getId());
        dto.setName(author.getName());
        dto.setCountry(author.getCountry());
        return dto;
    }

    public Set<InventionDto> convertInventionsToDto(Set<Invention> inventions) {
        return inventions.stream()
                .map(inventionMapper::convertToDto)
                .collect(Collectors.toSet());
    }

    public AuthorFullDto convertToFullDto(Author author) {
        AuthorFullDto dto = new AuthorFullDto();
        dto.setId(author.getId());
        dto.setName(author.getName());
        dto.setCountry(author.getCountry());

        if (author.getInventions() != null) {
            dto.setInventions(convertInventionsToDto(author.getInventions()));
        }

        return dto;
    }
}