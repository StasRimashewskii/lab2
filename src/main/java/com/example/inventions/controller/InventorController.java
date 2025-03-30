package com.example.inventions.controller;

import com.example.inventions.dto.InventorDto;
import com.example.inventions.service.InventorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Set;

@RestController
@RequestMapping("/inventions")
public class InventorController {

    private final InventorService inventorService;

    public InventorController(InventorService inventorService) {
        this.inventorService = inventorService;
    }

    @PostMapping("/{inventionId}/inventors")
    public ResponseEntity<InventorDto> addInventorToInvention(
            @PathVariable Long inventionId,
            @RequestBody InventorDto inventorDto) {
        InventorDto addedInventor = inventorService.addInventorToInvention(inventionId, inventorDto);
        return ResponseEntity.ok(addedInventor);
    }

    @DeleteMapping("/{inventionId}/inventors/{inventorId}")
    public ResponseEntity<Void> deleteInventorFromInvention(
            @PathVariable Long inventionId,
            @PathVariable Long inventorId) {
        inventorService.deleteInventorFromInvention(inventionId, inventorId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{inventionId}/inventors/{inventorId}")
    public ResponseEntity<InventorDto> updateInventorForInvention(
            @PathVariable Long inventionId,
            @PathVariable Long inventorId,
            @RequestBody InventorDto inventorDto) {
        InventorDto updatedInventor = inventorService
                .updateInventorForInvention(inventionId, inventorId, inventorDto);
        return ResponseEntity.ok(updatedInventor);
    }

    @GetMapping("/{inventionId}/inventors")
    public ResponseEntity<Set<InventorDto>> getAllInventorsForInvention(@PathVariable Long inventionId) {
        Set<InventorDto> inventors = inventorService.getAllInventorsForInvention(inventionId);
        return ResponseEntity.ok(inventors);
    }
}