package ru.content.management.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.content.management.dto.ArticleDTO;
import ru.content.management.dto.ArticleCreateDTO;
import ru.content.management.dto.ArticleUpdateDTO;
import ru.content.management.service.ArticleService;
import java.util.List;

@RestController
@RequestMapping("/articles")
@Tag(name = "Article", description = "API для управления статьями")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    @Operation(summary = "Поиск статей")
    @ResponseStatus(HttpStatus.OK)
    public List<ArticleDTO> find(
            @RequestParam(required = false) @Size(max = 255) String title,
            @RequestParam(required = false) @Size(max = 150) String author) {
        return articleService.find(title, author);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить статью по ID")
    @ResponseStatus(HttpStatus.OK)
    public ArticleDTO findById(@PathVariable Long id) {
        return articleService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Создать статью")
    @ResponseStatus(HttpStatus.CREATED)
    public ArticleDTO create(@Valid @RequestBody ArticleCreateDTO dto) {
        return articleService.create(dto);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновить статью")
    @ResponseStatus(HttpStatus.OK)
    public ArticleDTO update(@PathVariable Long id, @Valid @RequestBody ArticleUpdateDTO dto) {
        return articleService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить статью")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        articleService.delete(id);
    }
}
