package ru.content.management.service;

import ru.content.management.dto.ArticleDTO;
import ru.content.management.dto.ArticleCreateDTO;
import ru.content.management.dto.ArticleUpdateDTO;
import java.util.List;

public interface ArticleService {
    List<ArticleDTO> find(String title, String author);
    ArticleDTO findById(Long id);
    ArticleDTO create(ArticleCreateDTO dto);
    ArticleDTO update(Long id, ArticleUpdateDTO dto);
    void delete(Long id);
}
