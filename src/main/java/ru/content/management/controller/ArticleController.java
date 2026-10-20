package ru.content.management.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.content.management.dto.ArticleDTO;
import ru.content.management.dto.ArticleCreateDTO;
import ru.content.management.dto.ArticleUpdateDTO;
import ru.content.management.service.ArticleService;
import java.util.List;

@RestController
@RequestMapping("/article")
@Tag(name = "Article", description = "API для управления статьями")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/")
    @Operation(summary = "Получить все статьи")
    @ResponseStatus(HttpStatus.OK)
    public List<ArticleDTO> findAll() {
        return articleService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить статью по ID")
    @ResponseStatus(HttpStatus.OK)
    public ArticleDTO findById(@PathVariable Long id) {
        return articleService.findById(id);
    }

    @PostMapping("/create")
    @Operation(summary = "Создать статью")
    @ResponseStatus(HttpStatus.CREATED)
    public ArticleDTO create(@Valid @RequestBody ArticleCreateDTO dto) {
        return articleService.create(dto);
    }

    @PutMapping("/{id}")
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
