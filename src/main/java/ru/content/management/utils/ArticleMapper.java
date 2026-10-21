package ru.content.management.utils;

import lombok.experimental.UtilityClass;
import ru.content.management.entity.Article;
import ru.content.management.dto.ArticleDTO;
import ru.content.management.dto.ArticleCreateDTO;
import ru.content.management.dto.ArticleUpdateDTO;
import java.time.LocalDateTime;

@UtilityClass
public class ArticleMapper {

    public Article toEntity(ArticleDTO dto) {
        Article article = new Article();
        article.setId(dto.getId());
        article.setTitle(dto.getTitle());
        article.setText(dto.getText());
        article.setAuthor(dto.getAuthor());
        article.setPublishedAt(dto.getPublishedAt());
        return article;
    }

    public Article toEntityCreate(ArticleCreateDTO dto) {
        Article article = new Article();
        article.setTitle(dto.getTitle());
        article.setText(dto.getText());
        article.setAuthor(dto.getAuthor());
        article.setPublishedAt(LocalDateTime.now());
        return article;
    }

    public void updateEntity(ArticleUpdateDTO dto, Article article) {
        if (dto.getTitle() != null) article.setTitle(dto.getTitle());
        if (dto.getText() != null) article.setText(dto.getText());
        if (dto.getAuthor() != null) article.setAuthor(dto.getAuthor());
    }

    public ArticleDTO toDTO(Article article) {
        ArticleDTO dto = new ArticleDTO();
        dto.setId(article.getId());
        dto.setTitle(article.getTitle());
        dto.setText(article.getText());
        dto.setAuthor(article.getAuthor());
        dto.setPublishedAt(article.getPublishedAt());
        return dto;
    }
}
