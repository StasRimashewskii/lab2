package com.example.inventions.controller;

import com.example.inventions.dto.InventionDto;
import com.example.inventions.dto.InventionFullDto;
import com.example.inventions.service.InventionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventions")
@Tag(name = "Управление изобретениями", description = "API для работы с изобретениями, их категориями и авторами")
public class InventionController {

    private final InventionService inventionService;

    public InventionController(InventionService inventionService) {
        this.inventionService = inventionService;
    }

    @Operation(
            summary = "Получить все изобретения",
            description = "Возвращает полный список всех изобретений с детальной информацией"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список изобретений успешно получен",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InventionFullDto.class)))
    })
    @GetMapping("/all")
    public ResponseEntity<List<InventionFullDto>> getAllInventions() {
        return ResponseEntity.ok(inventionService.getAllInventions());
    }

    @Operation(
            summary = "Получить изобретение по ID",
            description = "Возвращает основную информацию об изобретении по его идентификатору"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Изобретение найдено",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InventionDto.class))),
            @ApiResponse(responseCode = "404", description = "Изобретение не найдено",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<InventionDto> getInventionById(
            @Parameter(description = "ID изобретения", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(inventionService.getInventionById(id));
    }

    @Operation(
            summary = "Поиск изобретений по названию",
            description = "Возвращает список изобретений, содержащих указанную строку в названии"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Изобретения найдены",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InventionDto.class)))
    })
    @GetMapping
    public ResponseEntity<List<InventionDto>> getInventionsByTitle(
            @Parameter(description = "Часть названия для поиска", required = true, example = "телефон")
            @RequestParam String title) {
        return ResponseEntity.ok(inventionService.findInventionsByTitle(title));
    }

    @Operation(
            summary = "Создать новое изобретение",
            description = "Добавляет новое изобретение в систему с указанием категорий"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Изобретение успешно создано",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InventionFullDto.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные изобретения",
                    content = @Content)
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Данные для создания изобретения",
            required = true,
            content = @Content(
                    schema = @Schema(implementation = InventionDto.class),
                    examples = @ExampleObject(
                            name = "Пример запроса",
                            summary = "Создание изобретения",
                            value = """
                {
                  "title": "Умная грядка с автополивом",
                  "description": "Автоматическая система выращивания растений с датчиками влажности, освещения и подачей воды в нужное время.",
                  "instruction": "1. Установите грядку в солнечном месте. 2. Заполните резервуар водой. 3. Выберите режим выращивания в приложении. 4. Следите за уведомлениями о необходимости добавки удобрений.",
                  "categories": [
                    {
                      "id": 1,
                      "name": "Технологии"
                    },
                    {
                      "id": 4,
                      "name": "Сад и огород"
                    },
                    {
                      "id": 5,
                      "name": "Экология"
                    }
                  ]
                }
                """
                    )
            )
    )
    @PostMapping
    public ResponseEntity<InventionFullDto> createInvention(
            @Parameter(hidden = true)
            @Valid @RequestBody InventionDto inventionDto) {
        return ResponseEntity.ok(inventionService.createInvention(inventionDto));
    }

    @Operation(
            summary = "Обновить изобретение",
            description = "Изменяет данные существующего изобретения и его категории"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Изобретение успешно обновлено",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InventionFullDto.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные изобретения",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Изобретение не найдено",
                    content = @Content)
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Новые данные для изобретения",
            required = true,
            content = @Content(
                    schema = @Schema(implementation = InventionDto.class),
                    examples = @ExampleObject(
                            name = "Пример запроса",
                            summary = "Обновление изобретения",
                            value = """
                {
                  "title": "Умная грядка PRO с автополивом",
                  "description": "Улучшенная версия с расширенными возможностями контроля микроклимата",
                  "instruction": "1. Установите грядку. 2. Настройте параметры в приложении. 3. Система будет автоматически поддерживать идеальные условия для растений.",
                  "categories": [
                    {
                      "id": 1,
                      "name": "Технологии"
                    },
                    {
                      "id": 4,
                      "name": "Сад и огород"
                    },
                    {
                      "id": 6,
                      "name": "Умный дом"
                    }
                  ]
                }
                """
                    )
            )
    )
    @PutMapping("/{inventionId}")
    public ResponseEntity<InventionFullDto> updateInvention(
            @Parameter(description = "ID изобретения для обновления", required = true, example = "1")
            @PathVariable Long inventionId,
            @Parameter(hidden = true)
            @Valid @RequestBody InventionDto inventionDto) {
        return ResponseEntity.ok(inventionService.updateInvention(inventionId, inventionDto));
    }

    @Operation(
            summary = "Удалить изобретение",
            description = "Удаляет изобретение из системы по его идентификатору"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Изобретение успешно удалено"),
            @ApiResponse(responseCode = "404", description = "Изобретение не найдено",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvention(
            @Parameter(description = "ID изобретения для удаления", required = true, example = "1")
            @PathVariable Long id) {
        inventionService.deleteInventionById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Удалить категорию у изобретения",
            description = "Удаляет связь между изобретением и указанной категорией"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Категория успешно удалена у изобретения"),
            @ApiResponse(responseCode = "404", description = "Изобретение или категория не найдены",
                    content = @Content)
    })
    @DeleteMapping("/{inventionId}/categories/{categoryId}")
    public ResponseEntity<Void> removeCategoryFromInvention(
            @Parameter(description = "ID изобретения", required = true, example = "1")
            @PathVariable Long inventionId,
            @Parameter(description = "ID категории", required = true, example = "2")
            @PathVariable Long categoryId) {
        inventionService.removeCategoryFromInvention(inventionId, categoryId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Поиск по нескольким категориям",
            description = "Возвращает изобретения, относящиеся к любой из указанных категорий"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Изобретения найдены",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InventionDto.class)))
    })
    @GetMapping("/search/multiple-categories")
    public ResponseEntity<List<InventionDto>> findInventionsByCategoryNames(
            @Parameter(description = "Список названий категорий", required = true, example = "[\"Технологии\",\"Электроника\"]")
            @RequestParam List<String> categoryNames) {
        return ResponseEntity.ok(inventionService.findInventionsByCategoryNames(categoryNames));
    }

    @Operation(
            summary = "Поиск по точному совпадению категорий",
            description = "Возвращает изобретения, относящиеся ко всем указанным категориям одновременно"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Изобретения найдены",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InventionDto.class)))
    })
    @GetMapping("/search/exact-categories")
    public ResponseEntity<List<InventionDto>> findInventionsByExactCategories(
            @Parameter(description = "Список названий категорий для точного поиска", required = true, example = "[\"Технологии\",\"Мобильные устройства\"]")
            @RequestParam List<String> categoryNames) {
        return ResponseEntity.ok(inventionService.findInventionsByExactCategories(categoryNames));
    }

    @Operation(
            summary = "Поиск по стране автора",
            description = "Возвращает изобретения, авторы которых из указанной страны"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Изобретения найдены",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InventionDto.class)))
    })
    @GetMapping("/search/authors-country")
    public ResponseEntity<List<InventionDto>> findInventionsByAuthorsCountry(
            @Parameter(description = "Страна для поиска", required = true, example = "США")
            @RequestParam String country) {
        return ResponseEntity.ok(inventionService.findInventionsByAuthorCountry(country));
    }

    @Operation(
            summary = "Нативный поиск по названию",
            description = "Возвращает изобретения, найденные с помощью нативного SQL запроса по названию"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Изобретения найдены",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InventionDto.class)))
    })
    @GetMapping("/native")
    public ResponseEntity<List<InventionDto>> getInventionsByTitleNative(
            @Parameter(description = "Часть названия для нативного поиска", required = true, example = "телефон")
            @RequestParam String title) {
        return ResponseEntity.ok(inventionService.findInventionsByTitleNative(title));
    }

    @Operation(
            summary = "Создать несколько изобретений",
            description = "Добавляет список новых изобретений в систему (bulk-операция)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Изобретения успешно созданы",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InventionFullDto.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные изобретений",
                    content = @Content)
    })
    @PostMapping("/bulk")
    public ResponseEntity<List<InventionFullDto>> createInventionsBulk(
            @Parameter(description = "Список изобретений для создания", required = true)
            @Valid @RequestBody List<InventionDto> inventionDtos) {

        List<InventionFullDto> createdInventions = inventionDtos.stream()
                .map(inventionService::createInvention)
                .toList();

        return ResponseEntity.ok(createdInventions);
    }

}