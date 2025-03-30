package com.example.inventions.mapper;

import com.example.inventions.dto.InventorDto;
import com.example.inventions.entity.Inventor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class InventorMapper {

    public Set<InventorDto> convertToDto(Set<Inventor> inventors) {
        if (inventors == null) {
            return new HashSet<>();
        }
        return inventors.stream()
                .map(this::convertToDto)
                .collect(Collectors.toSet());
    }

    public InventorDto convertToDto(Inventor inventor) {
        InventorDto dto = new InventorDto();
        dto.setId(inventor.getId());
        dto.setName(inventor.getName());
        dto.setExpertise(inventor.getExpertise());
        return dto;
    }
}