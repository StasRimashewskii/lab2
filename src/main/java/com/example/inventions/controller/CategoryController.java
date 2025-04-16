package com.example.inventions.controller;

import com.example.inventions.dto.CategoryDto;
import com.example.inventions.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
@Tag(name = "Управление категориями", description = "API для работы с категориями изобретений")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(
            summary = "Удалить категорию",
            description = "Удаляет категорию по её идентификатору"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Категория успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Категория не найдена",
                    content = @Content)
    })
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(
            @Parameter(description = "ID категории для удаления", required = true, example = "1")
            @PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Создать новую категорию",
            description = "Добавляет новую категорию в систему"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Категория успешно создана",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDto.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные категории",
                    content = @Content)
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Данные для создания категории",
            required = true,
            content = @Content(
                    schema = @Schema(implementation = CategoryDto.class),
                    examples = @ExampleObject(
                            name = "Пример запроса",
                            summary = "Создание категории",
                            value = """
                    {
                        "name": "Технологии",
                        "description": "Современные технологические изобретения"
                    }
                    """
                    )
            )
    )
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(
            @Parameter(hidden = true)
            @Valid @RequestBody CategoryDto categoryDto) {
        return ResponseEntity.ok(categoryService.createCategory(categoryDto));
    }

    @Operation(
            summary = "Обновить категорию",
            description = "Обновляет данные существующей категории"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Категория успешно обновлена",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDto.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные категории",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Категория не найдена",
                    content = @Content)
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Новые данные для категории",
            required = true,
            content = @Content(
                    schema = @Schema(implementation = CategoryDto.class),
                    examples = @ExampleObject(
                            name = "Пример запроса",
                            summary = "Обновление категории",
                            value = """
                    {
                        "name": "Обновленные технологии"
                    }
                    """
                    )
            )
    )
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(
            @Parameter(description = "ID категории для обновления", required = true, example = "1")
            @PathVariable Long categoryId,
            @Parameter(hidden = true)
            @Valid @RequestBody CategoryDto categoryDto) {
        CategoryDto updatedCategory = categoryService.updateCategory(categoryId, categoryDto);
        return ResponseEntity.ok(updatedCategory);
    }
}