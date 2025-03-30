package com.example.inventions.service;

import com.example.inventions.dto.InventorDto;
import com.example.inventions.entity.Invention;
import com.example.inventions.entity.Inventor;
import com.example.inventions.repository.InventionRepository;
import com.example.inventions.repository.InventorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class InventorService {

    private final InventorRepository inventorRepository;
    private final InventionRepository inventionRepository;

    public InventorService(InventorRepository inventorRepository,
                           InventionRepository inventionRepository) {
        this.inventorRepository = inventorRepository;
        this.inventionRepository = inventionRepository;
    }

    @Transactional
    public InventorDto addInventorToInvention(Long inventionId, InventorDto inventorDto) {
        Invention invention = inventionRepository.findById(inventionId)
                .orElseThrow(() -> new EntityNotFoundException("Invention not found with id " + inventionId));

        Inventor inventor = new Inventor();
        inventor.setName(inventorDto.getName());
        inventor.setExpertise(inventorDto.getExpertise());
        inventor.setInvention(invention);

        Inventor savedInventor = inventorRepository.save(inventor);
        return convertToDto(savedInventor);
    }

    @Transactional
    public void deleteInventorFromInvention(Long inventionId, Long inventorId) {
        Inventor inventor = inventorRepository.findById(inventorId)
                .orElseThrow(() -> new EntityNotFoundException("Inventor not found with id " + inventorId));

        if (!inventor.getInvention().getId().equals(inventionId)) {
            throw new IllegalArgumentException("Inventor does not belong to the specified invention");
        }

        inventorRepository.delete(inventor);
    }

    @Transactional
    public InventorDto updateInventorForInvention(Long inventionId, Long inventorId, InventorDto inventorDto) {
        Inventor inventor = inventorRepository.findById(inventorId)
                .orElseThrow(() -> new EntityNotFoundException("Inventor not found with id " + inventorId));

        if (!inventor.getInvention().getId().equals(inventionId)) {
            throw new IllegalArgumentException("Inventor does not belong to the specified invention");
        }

        inventor.setName(inventorDto.getName());
        inventor.setExpertise(inventorDto.getExpertise());
        Inventor updatedInventor = inventorRepository.save(inventor);

        return convertToDto(updatedInventor);
    }

    @Transactional(readOnly = true)
    public Set<InventorDto> getAllInventorsForInvention(Long inventionId) {
        Invention invention = inventionRepository.findById(inventionId)
                .orElseThrow(() -> new EntityNotFoundException("Invention not found with id " + inventionId));

        return inventorRepository.findByInvention(invention).stream()
                .map(this::convertToDto)
                .collect(Collectors.toSet());
    }

    private InventorDto convertToDto(Inventor inventor) {
        return new InventorDto(
                inventor.getId(),
                inventor.getName(),
                inventor.getExpertise()
        );
    }
}