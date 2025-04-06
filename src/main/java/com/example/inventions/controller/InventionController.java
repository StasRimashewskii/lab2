package com.example.inventions.controller;

import com.example.inventions.dto.InventionDto;
import com.example.inventions.dto.InventionFullDto;
import com.example.inventions.service.InventionService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventions")
public class InventionController {

    private final InventionService inventionService;

    public InventionController(InventionService inventionService) {
        this.inventionService = inventionService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<InventionFullDto>> getAllInventions() {
        List<InventionFullDto> inventions = inventionService.getAllInventions();
        return ResponseEntity.ok(inventions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventionDto> getInventionById(@PathVariable Long id) {
        return ResponseEntity.ok(inventionService.getInventionById(id));
    }

    @GetMapping
    public ResponseEntity<List<InventionDto>> getInventionsByTitle(@RequestParam String title) {
        return ResponseEntity.ok(inventionService.findInventionsByTitle(title));
    }

    @PostMapping
    public ResponseEntity<InventionFullDto> createInvention(@RequestBody InventionDto inventionDto) {
        return ResponseEntity.ok(inventionService.createInvention(inventionDto));
    }

    @PutMapping("/{inventionId}")
    public ResponseEntity<InventionFullDto> updateInvention(
            @PathVariable Long inventionId,
            @RequestBody InventionDto inventionDto) {
        return ResponseEntity.ok(inventionService.updateInvention(inventionId, inventionDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvention(@PathVariable Long id) {
        inventionService.deleteInventionById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{inventionId}/categories/{categoryId}")
    public ResponseEntity<Void> removeCategoryFromInvention(
            @PathVariable Long inventionId,
            @PathVariable Long categoryId) {
        inventionService.removeCategoryFromInvention(inventionId, categoryId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search/multiple-categories")
    public ResponseEntity<List<InventionDto>> findInventionsByCategoryNames(
            @RequestParam List<String> categoryNames) {
        List<InventionDto> inventions = inventionService.findInventionsByCategoryNames(categoryNames);
        return ResponseEntity.ok(inventions);
    }

    @GetMapping("/search/exact-categories")
    public ResponseEntity<List<InventionDto>> findInventionsByExactCategories(@RequestParam List<String> categoryNames) {
        List<InventionDto> inventions = inventionService.findInventionsByExactCategories(categoryNames);
        return ResponseEntity.ok(inventions);
    }


    @GetMapping("/search/authors-country")
    public ResponseEntity<List<InventionDto>> findInventionsByAuthorsCountry(@RequestParam String country) {
        List<InventionDto> inventions = inventionService.findInventionsByAuthorsCountry(country);
        return ResponseEntity.ok(inventions);
    }

}