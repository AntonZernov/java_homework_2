package ru.content.management.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ArticleUpdateDTO {
    @Size(max = 255)
    private String title;
    private String text;
    @Size(max = 150)
    private String author;
}
