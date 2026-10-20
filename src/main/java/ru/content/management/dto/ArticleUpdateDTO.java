package ru.content.management.dto;

import lombok.Data;

@Data
public class ArticleUpdateDTO {
    private String title;
    private String text;
    private String author;
}
