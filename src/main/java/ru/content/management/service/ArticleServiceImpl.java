package ru.content.management.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.content.management.entity.Article;
import ru.content.management.exception.EntityNotFoundException;
import ru.content.management.repository.ArticleRepository;
import ru.content.management.dto.ArticleDTO;
import ru.content.management.dto.ArticleCreateDTO;
import ru.content.management.dto.ArticleUpdateDTO;
import ru.content.management.mapper.ArticleMapper;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;

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
                .orElseThrow(() -> new EntityNotFoundException("Статья не найдена"));
    }

    @Override
    public ArticleDTO create(ArticleCreateDTO dto) {
        Article article = articleMapper.toEntityCreate(dto);
        return articleMapper.toDTO(articleRepository.save(article));
    }

    @Override
    public ArticleDTO update(Long id, ArticleUpdateDTO dto) {
        Article existing = articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Статья для редактирования не найдена"));
        articleMapper.updateEntity(dto, existing);
        return articleMapper.toDTO(articleRepository.save(existing));
    }

    @Override
    public void delete(Long id) {
        articleRepository.deleteById(id);
    }
}
