package ru.content.management.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalTime;

@Data
public class PodcastEpisodeDTO {
    private Long id;
    private Long podcastId;
    private Integer episodeNumber;
    private String title;
    private String audioUrl;
    @Schema(example = "00:04:24")
    private LocalTime duration;
}
