package ru.content.management.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.content.management.dto.ArticleCreateDTO;
import ru.content.management.dto.ArticleDTO;
import ru.content.management.dto.ArticleUpdateDTO;
import ru.content.management.entity.Article;
import ru.content.management.exception.EntityNotFoundException;
import ru.content.management.repository.ArticleRepository;
import ru.content.management.utils.ArticleMapper;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class ArticleServiceImplTest {

    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private ArticleServiceImpl articleService;

    @Test
    @SuppressWarnings("unchecked")
    void findSuccessTest() {
        Article article = createArticle();
        ArticleDTO dto = createArticleDTO();

        when(articleRepository.findAll((Specification<Article>) any())).thenReturn(List.of(article));
        try (MockedStatic<ArticleMapper> mockedMapper = Mockito.mockStatic(ArticleMapper.class)) {
            mockedMapper.when(() -> ArticleMapper.toDTO(article)).thenReturn(dto);

            List<ArticleDTO> result = articleService.find(null, null);

            assertEquals(1, result.size());
            assertEquals("Test Title", result.getFirst().getTitle());
            verify(articleRepository).findAll((Specification<Article>) any());
        }
    }

    private Article createArticle() {
        Article article = new Article();
        article.setId(1L);
        article.setTitle("Test Title");
        article.setText("Test Text");
        article.setAuthor("Test Author");
        article.setPublishedAt(LocalDateTime.of(2025, 1, 1, 0, 0));
        return article;
    }

    private ArticleDTO createArticleDTO() {
        ArticleDTO dto = new ArticleDTO();
        dto.setId(1L);
        dto.setTitle("Test Title");
        dto.setText("Test Text");
        dto.setAuthor("Test Author");
        dto.setPublishedAt(LocalDateTime.of(2025, 1, 1, 0, 0));
        return dto;
    }

    @Test
    @SuppressWarnings("unchecked")
    void findSuccessEmptyTest() {
        when(articleRepository.findAll((Specification<Article>) any())).thenReturn(List.of());

        List<ArticleDTO> result = articleService.find(null, null);

        assertTrue(result.isEmpty());
        verify(articleRepository).findAll((Specification<Article>) any());
    }

    @Test
    void findByIdSuccessTest() {
        Article article = createArticle();
        ArticleDTO dto = createArticleDTO();

        when(articleRepository.findById(1L)).thenReturn(java.util.Optional.of(article));
        try (MockedStatic<ArticleMapper> mockedMapper = Mockito.mockStatic(ArticleMapper.class)) {
            mockedMapper.when(() -> ArticleMapper.toDTO(article)).thenReturn(dto);
            ArticleDTO result = articleService.findById(1L);
            assertNotNull(result);
            assertEquals("Test Title", result.getTitle());
            verify(articleRepository).findById(1L);
        }
    }

    @Test
    void findByIdNotFoundExceptionTest() {
        when(articleRepository.findById(99L)).thenReturn(java.util.Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            articleService.findById(99L);
        });

        assertEquals("Статья не найдена", exception.getMessage());
        verify(articleRepository).findById(99L);
    }

    @Test
    void createSuccessTest() {
        ArticleCreateDTO createDTO = new ArticleCreateDTO();
        createDTO.setTitle("New Title");
        createDTO.setText("New Text");
        createDTO.setAuthor("New Author");

        Article entity = new Article();
        entity.setTitle("New Title");
        entity.setText("New Text");
        entity.setAuthor("New Author");
        entity.setPublishedAt(LocalDateTime.now());

        ArticleDTO savedDTO = new ArticleDTO();
        savedDTO.setId(1L);
        savedDTO.setTitle("New Title");
        savedDTO.setText("New Text");
        savedDTO.setAuthor("New Author");
        savedDTO.setPublishedAt(entity.getPublishedAt());
        try (MockedStatic<ArticleMapper> mockedMapper = Mockito.mockStatic(ArticleMapper.class)) {
            mockedMapper.when(() -> ArticleMapper.toEntityCreate(createDTO)).thenReturn(entity);
            when(articleRepository.save(entity)).thenReturn(entity);
            when(ArticleMapper.toDTO(entity)).thenReturn(savedDTO);

            ArticleDTO result = articleService.create(createDTO);

            assertNotNull(result);
            assertEquals(1L, result.getId());
            verify(articleRepository).save(entity);
        }
    }

    @Test
    void updateAllFieldsSuccessTest() {
        ArticleUpdateDTO updateDTO = new ArticleUpdateDTO();
        updateDTO.setTitle("Updated Title");
        updateDTO.setText("Updated Text");

        Article existing = createArticle();

        Article saved = new Article();
        saved.setId(1L);
        saved.setTitle("Updated Title");
        saved.setText("Updated Text");
        saved.setAuthor("Test Author");
        saved.setPublishedAt(LocalDateTime.of(2025, 1, 1, 0, 0));

        ArticleDTO savedDTO = new ArticleDTO();
        savedDTO.setId(1L);
        savedDTO.setTitle("Updated Title");
        savedDTO.setText("Updated Text");
        savedDTO.setAuthor("Test Author");
        savedDTO.setPublishedAt(saved.getPublishedAt());

        when(articleRepository.findById(1L)).thenReturn(java.util.Optional.of(existing));
        when(articleRepository.save(existing)).thenReturn(saved);
        try (MockedStatic<ArticleMapper> mockedMapper = Mockito.mockStatic(ArticleMapper.class)) {
            mockedMapper.when(() -> ArticleMapper.toDTO(saved)).thenReturn(savedDTO);

            ArticleDTO result = articleService.update(1L, updateDTO);

            assertNotNull(result);
            assertEquals("Updated Title", result.getTitle());
            assertEquals("Updated Text", result.getText());
            verify(articleRepository).findById(1L);
            verify(articleRepository).save(existing);
        }
    }

    @Test
    void updateNotFoundExceptionTest() {
        ArticleUpdateDTO updateDTO = new ArticleUpdateDTO();
        updateDTO.setTitle("Updated Title");

        when(articleRepository.findById(99L)).thenReturn(java.util.Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            articleService.update(99L, updateDTO);
        });

        assertEquals("Статья для редактирования не найдена", exception.getMessage());
        verify(articleRepository).findById(99L);
    }

    @Test
    void updateNonNullFieldsSuccessTest() {
        ArticleUpdateDTO updateDTO = new ArticleUpdateDTO();
        updateDTO.setTitle("Updated Title");

        Article existing = createArticle();

        Article saved = new Article();
        saved.setId(1L);
        saved.setTitle("Updated Title");
        saved.setText("Test Text");
        saved.setAuthor("Test Author");
        saved.setPublishedAt(LocalDateTime.of(2025, 1, 1, 0, 0));

        ArticleDTO savedDTO = new ArticleDTO();
        savedDTO.setId(1L);
        savedDTO.setTitle("Updated Title");
        savedDTO.setText("Test Text");
        savedDTO.setAuthor("Test Author");
        savedDTO.setPublishedAt(saved.getPublishedAt());

        when(articleRepository.findById(1L)).thenReturn(java.util.Optional.of(existing));
        when(articleRepository.save(existing)).thenReturn(saved);
        try (MockedStatic<ArticleMapper> mockedMapper = Mockito.mockStatic(ArticleMapper.class)) {
            mockedMapper.when(() -> ArticleMapper.toDTO(saved)).thenReturn(savedDTO);

            ArticleDTO result = articleService.update(1L, updateDTO);

            assertNotNull(result);
            assertEquals("Updated Title", result.getTitle());
            assertEquals("Test Text", result.getText());
        }
    }

    @Test
    void deleteSuccessTest() {
        when(articleRepository.existsById(1L)).thenReturn(true);

        articleService.delete(1L);

        verify(articleRepository).existsById(1L);
        verify(articleRepository).deleteById(1L);
    }

    @Test
    void deleteNotFoundExceptionTest() {
        when(articleRepository.existsById(99L)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            articleService.delete(99L);
        });

        assertEquals("Статья для удаления не найдена", exception.getMessage());
        verify(articleRepository).existsById(99L);
        verify(articleRepository, never()).deleteById(anyLong());
    }
}
