package ru.content.management.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import ru.content.management.entity.Article;
import ru.content.management.service.ArticleService;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/article")
@Tag(name = "Статьи", description = "API для управления статьями")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    @Operation(summary = "Получить все статьи")
    public List<Article> findAll() {
        return articleService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить статью по ID")
    public Optional<Article> findById(@PathVariable Long id) {
        return articleService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Создать статью")
    public Article create(@RequestBody Article article) {
        return articleService.create(article);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить статью")
    public Article update(@PathVariable Long id, @RequestBody Article article) {
        return articleService.update(id, article);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить статью")
    public void delete(@PathVariable Long id) {
        articleService.delete(id);
    }
}
