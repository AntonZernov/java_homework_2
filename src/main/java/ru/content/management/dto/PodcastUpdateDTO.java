package ru.content.management.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PodcastUpdateDTO {
    @Size(max = 255)
    private String title;
}
