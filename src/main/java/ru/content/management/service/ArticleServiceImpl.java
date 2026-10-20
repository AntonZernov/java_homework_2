package ru.content.management.service;

import org.springframework.stereotype.Service;
import ru.content.management.entity.Article;
import ru.content.management.exception.DatabaseException;
import ru.content.management.repository.ArticleRepository;
import ru.content.management.dto.ArticleDTO;
import ru.content.management.dto.ArticleCreateDTO;
import ru.content.management.dto.ArticleUpdateDTO;
import ru.content.management.mapper.ArticleMapper;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;

    public ArticleServiceImpl(ArticleRepository articleRepository, ArticleMapper articleMapper) {
        this.articleRepository = articleRepository;
        this.articleMapper = articleMapper;
    }

    @Override
    public List<ArticleDTO> findAll() {
        return articleRepository.findAll().stream()
                .map(articleMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ArticleDTO findById(Long id) {
        return articleRepository.findById(id)
                .map(articleMapper::toDTO)
                .orElseThrow(() -> new DatabaseException("Статья не найдена"));
    }

    @Override
    public ArticleDTO create(ArticleCreateDTO dto) {
        try {
            Article article = articleMapper.toEntityCreate(dto);
            return articleMapper.toDTO(articleRepository.save(article));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new DatabaseException("Ошибка при сохранении статьи");
        }
    }

    @Override
    public ArticleDTO update(Long id, ArticleUpdateDTO dto) {
        Article existing = articleRepository.findById(id)
                .orElseThrow(() -> new DatabaseException("Статья для редактирования не найдена"));
        articleMapper.updateEntity(dto, existing);
        return articleMapper.toDTO(articleRepository.save(existing));
    }

    @Override
    public void delete(Long id) {
        articleRepository.deleteById(id);
    }
}
