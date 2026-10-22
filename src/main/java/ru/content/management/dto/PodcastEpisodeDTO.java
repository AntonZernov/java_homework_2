package ru.content.management.dto;

import lombok.Data;

@Data
public class PodcastEpisodeDTO {
    private Long id;
    private Long podcastId;
    private Integer episodeNumber;
    private String title;
    private String audioUrl;
    private Integer durationSeconds;
}
