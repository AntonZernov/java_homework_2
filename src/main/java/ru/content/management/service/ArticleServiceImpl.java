package ru.content.management.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import ru.content.management.entity.Article;
import ru.content.management.exception.EntityNotFoundException;
import ru.content.management.repository.ArticleRepository;
import ru.content.management.dto.ArticleDTO;
import ru.content.management.dto.ArticleCreateDTO;
import ru.content.management.dto.ArticleUpdateDTO;
import ru.content.management.utils.ArticleMapper;
import ru.content.management.utils.SpecificationUtils;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    @Override
    public List<ArticleDTO> find(String title, String author) {
        Specification<Article> spec = Specification.where(null);

        if (StringUtils.hasText(title)) {
            spec = spec.and(SpecificationUtils.buildLikeSpec(title, "title"));
        }

        if (StringUtils.hasText(author)) {
            spec = spec.and(SpecificationUtils.buildLikeSpec(author, "author"));
        }

        return articleRepository.findAll(spec).stream()
                .map(ArticleMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ArticleDTO findById(Long id) {
        return articleRepository.findById(id)
                .map(ArticleMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Статья не найдена"));
    }

    @Override
    public ArticleDTO create(ArticleCreateDTO dto) {
        Article article = ArticleMapper.toEntityCreate(dto);
        return ArticleMapper.toDTO(articleRepository.save(article));
    }

    @Override
    public ArticleDTO update(Long id, ArticleUpdateDTO dto) {
        Article existing = articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Статья для редактирования не найдена"));
        ArticleMapper.updateEntity(dto, existing);
        return ArticleMapper.toDTO(articleRepository.save(existing));
    }

    @Override
    public void delete(Long id) {
        if (!articleRepository.existsById(id)) {
            throw new EntityNotFoundException("Статья для удаления не найдена");
        }
        articleRepository.deleteById(id);
    }
}
