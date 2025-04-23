package com.example.inventions.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/logs")
@Tag(name = "Управление логами", description = "API для работы с логами приложения")
public class LogController {

    @Operation(
            summary = "Получить логи по дате",
            description = "Возвращает список логов, содержащих указанную дату",
            parameters = {
                    @Parameter(
                            name = "date",
                            description = "Дата для фильтрации логов",
                            required = true,
                            example = "2025-05-15"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Логи успешно получены",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "[\"2023-05-15 10:00:00 INFO Application started\", \"2023-05-15 10:05:00 DEBUG Processing request\"]"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Ошибка при чтении файла логов"
            )
    })
    @GetMapping
    public ResponseEntity<List<String>> getLogsByDate(
            @RequestParam LocalDate date) throws IOException {

        String logPath = "logs/app.log";
        List<String> logs = Files.readAllLines(Paths.get(logPath))
                .stream()
                .filter(line -> line.contains(date.toString()))
                .collect(Collectors.toList());

        // Создание директории для логов, если её нет
        Path logsDir = Paths.get("logs", date.toString());
        if (Files.notExists(logsDir)) {
            Files.createDirectories(logsDir);
        }

        // Запись логов в новый файл
        Path logFile = logsDir.resolve("logs_" + date + ".log");
        Files.write(logFile, logs);

        return ResponseEntity.ok(logs);
    }
}
