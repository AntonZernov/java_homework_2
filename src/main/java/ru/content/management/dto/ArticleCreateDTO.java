package ru.content.management.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ArticleCreateDTO {
    @NotBlank
    @Size(max = 255)
    private String title;
    @NotBlank
    private String text;
    @NotBlank
    @Size(max = 150)
    private String author;
}
