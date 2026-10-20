package ru.content.management.service;

import ru.content.management.entity.Article;
import java.util.List;
import java.util.Optional;

public interface ArticleService {
    List<Article> findAll();
    Optional<Article> findById(Long id);
    Article create(Article article);
    Article update(Long id, Article article);
    void delete(Long id);
}
