package com.example.inventions.mapper;

import com.example.inventions.dto.AuthorDto;
import com.example.inventions.entity.Author;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {

    public Set<AuthorDto> convertToDto(Set<Author> authors) {
        if (authors == null) {
            return new HashSet<>();
        }
        return authors.stream()
                .map(this::convertToDto)
                .collect(Collectors.toSet());
    }

    public AuthorDto convertToDto(Author author) {
        AuthorDto dto = new AuthorDto();
        dto.setId(author.getId());
        dto.setName(author.getName());
        dto.setCountry(author.getCountry());
        return dto;
    }
}