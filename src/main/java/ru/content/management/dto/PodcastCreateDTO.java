package ru.content.management.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PodcastCreateDTO {
    @NotBlank
    @Size(max = 255)
    private String title;
}
