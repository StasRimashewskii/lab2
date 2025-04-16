package com.example.inventions.controller;

import com.example.inventions.dto.AuthorDto;
import com.example.inventions.dto.AuthorFullDto;
import com.example.inventions.dto.InventionDto;
import com.example.inventions.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authors")
@Tag(name = "Управление авторами", description = "API для работы с авторами и их изобретениями")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @Operation(
            summary = "Получить авторов по стране",
            description = "Возвращает список авторов, отфильтрованных по стране происхождения"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Авторы успешно получены",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthorDto.class))),
            @ApiResponse(responseCode = "404", description = "Авторы для указанной страны не найдены",
                    content = @Content)
    })
    @GetMapping("/country/{country}")
    public ResponseEntity<List<AuthorDto>> getAuthorsByCountry(
            @Parameter(description = "Название страны для фильтрации", required = true, example = "Россия")
            @PathVariable String country) {
        return ResponseEntity.ok(authorService.getAuthorsByCountry(country));
    }

    @Operation(
            summary = "Получить автора с изобретениями",
            description = "Возвращает полную информацию об авторе, включая его изобретения"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация об авторе успешно получена",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthorFullDto.class))),
            @ApiResponse(responseCode = "404", description = "Автор не найден",
                    content = @Content)
    })
    @GetMapping("/{authorId}")
    public ResponseEntity<AuthorFullDto> getAuthorWithInventions(
            @Parameter(description = "ID автора", required = true, example = "1")
            @PathVariable Long authorId) {
        return ResponseEntity.ok(authorService.getAuthorWithInventions(authorId));
    }

    @Operation(
            summary = "Создать нового автора",
            description = "Добавляет нового автора в систему"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Автор успешно создан",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthorDto.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные автора",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<AuthorDto> createAuthor(
            @Parameter(description = "Данные автора для создания", required = true)
            @Valid @RequestBody AuthorDto authorDto) {
        return ResponseEntity.ok(authorService.createAuthor(authorDto));
    }

    @Operation(
            summary = "Добавить изобретение автору",
            description = "Связывает существующее изобретение с автором"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Изобретение успешно добавлено автору",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthorDto.class))),
            @ApiResponse(responseCode = "404", description = "Автор или изобретение не найдены",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос на связывание",
                    content = @Content)
    })
    @PostMapping("/{authorId}/inventions/{inventionId}")
    public ResponseEntity<AuthorDto> addInventionToAuthor(
            @Parameter(description = "ID автора", required = true, example = "1")
            @PathVariable Long authorId,
            @Parameter(description = "ID изобретения для добавления", required = true, example = "5")
            @PathVariable Long inventionId) {
        return ResponseEntity.ok(authorService.addInventionToAuthor(authorId, inventionId));
    }

    @Operation(
            summary = "Удалить автора",
            description = "Удаляет запись об авторе из системы"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Автор успешно удален"),
            @ApiResponse(responseCode = "404", description = "Автор не найден",
                    content = @Content)
    })
    @DeleteMapping("/{authorId}")
    public ResponseEntity<Void> deleteAuthor(
            @Parameter(description = "ID автора для удаления", required = true, example = "1")
            @PathVariable Long authorId) {
        authorService.removeAuthor(authorId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Получить изобретения автора",
            description = "Возвращает все изобретения, связанные с указанным автором"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Изобретения успешно получены",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InventionDto.class))),
            @ApiResponse(responseCode = "404", description = "Автор не найден",
                    content = @Content)
    })
    @GetMapping("/{authorId}/inventions")
    public ResponseEntity<List<InventionDto>> getInventionsByAuthor(
            @Parameter(description = "ID автора", required = true, example = "1")
            @PathVariable Long authorId) {
        return ResponseEntity.ok(authorService.getInventionsByAuthor(authorId));
    }

    @Operation(
            summary = "Получить автора по имени",
            description = "Возвращает автора по его полному имени (с учетом регистра)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Автор успешно найден",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthorDto.class))),
            @ApiResponse(responseCode = "404", description = "Автор не найден",
                    content = @Content)
    })
    @GetMapping("/name/{name}")
    public ResponseEntity<AuthorDto> getAuthorByName(
            @Parameter(description = "Полное имя автора", required = true, example = "Иван Петров")
            @PathVariable String name) {
        return ResponseEntity.ok(authorService.getAuthorByName(name));
    }
}