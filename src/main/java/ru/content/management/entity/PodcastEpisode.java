package ru.content.management.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "podcast_episodes")
public class PodcastEpisode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "podcast_id", nullable = false)
    private Long podcastId;

    @Column(name = "episode_number", nullable = false)
    private Integer episodeNumber;

    @Column(nullable = false)
    private String title;

    @Column(name = "audio_url", nullable = false)
    private String audioUrl;

    @Column(name = "duration", nullable = false, columnDefinition = "TIME(0)")
    private LocalTime duration;
}
