package com.example.inventions.controller;

import com.example.inventions.model.Invention;
import com.example.inventions.service.InventionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inventions")
public class InventionController {

    private final InventionService inventionService;

    public InventionController(InventionService inventionService) {
        this.inventionService = inventionService;
    }

    // GET запрос: получить список всех изобретений
    @GetMapping
    public ResponseEntity<List<Invention>> getAllInventions() {
        return ResponseEntity.ok(inventionService.getAllInventions());
    }

    // GET запрос с Path Parameters: получить изобретение по ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> getInventionById(@PathVariable Long id) {
        Invention invention = inventionService.getInventionById(id);
        if (invention == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "404 Not Found");
            errorResponse.put("message", "Изобретение с ID " + id + " не найдено.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
        return ResponseEntity.ok(invention);
    }

    // GET запрос с Query Parameters: поиск изобретений по имени
    @GetMapping("/search")
    public ResponseEntity<List<Invention>> searchInventions(@RequestParam(name = "name", required = false) String name) {
        List<Invention> inventions = inventionService.getAllInventions();
        if (name != null && !name.isEmpty()) {
            inventions = inventions.stream()
                    .filter(invention -> invention.getName().toLowerCase().contains(name.toLowerCase()))
                    .toList();
        }
        return ResponseEntity.ok(inventions);
    }
}