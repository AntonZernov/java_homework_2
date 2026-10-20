package ru.content.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.content.management.entity.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {}
