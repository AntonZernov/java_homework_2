package ru.content.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.content.management.entity.PodcastEpisode;
import java.util.List;

public interface PodcastEpisodeRepository extends JpaRepository<PodcastEpisode, Long>, JpaSpecificationExecutor<PodcastEpisode> {
    List<PodcastEpisode> findByPodcastId(Long podcastId);
}
