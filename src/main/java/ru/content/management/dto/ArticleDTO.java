package ru.content.management.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ArticleDTO {
    private Long id;
    private String title;
    private String text;
    private String author;
    private LocalDateTime publishedAt;
}
