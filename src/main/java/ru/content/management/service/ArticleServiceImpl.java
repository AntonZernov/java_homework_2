package ru.content.management.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.content.management.entity.Article;
import ru.content.management.repository.ArticleRepository;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {


    private final ArticleRepository articleRepository;

    @Override
    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    @Override
    public Optional<Article> findById(Long id) {
        return articleRepository.findById(id);
    }

    @Override
    public Article create(Article article) {
        return articleRepository.save(article);
    }

    @Override
    public Article update(Long id, Article article) {
        Article existing = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found"));
        if (article.getTitle() != null) existing.setTitle(article.getTitle());
        if (article.getText() != null) existing.setText(article.getText());
        if (article.getAuthor() != null) existing.setAuthor(article.getAuthor());
        if (article.getPublishedAt() != null) existing.setPublishedAt(article.getPublishedAt());
        return articleRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        articleRepository.deleteById(id);
    }
}
