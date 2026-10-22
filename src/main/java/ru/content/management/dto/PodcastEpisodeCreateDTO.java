package ru.content.management.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PodcastEpisodeCreateDTO {

    @NotNull
    private Integer episodeNumber;

    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    @Size(max = 1024)
    private String audioUrl;

    @NotNull
    private Integer durationSeconds;
}
