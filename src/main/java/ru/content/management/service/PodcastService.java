package ru.content.management.service;

import ru.content.management.dto.PodcastCreateDTO;
import ru.content.management.dto.PodcastDTO;
import ru.content.management.dto.PodcastEpisodeCreateDTO;
import ru.content.management.dto.PodcastEpisodeDTO;
import ru.content.management.dto.PodcastEpisodeUpdateDTO;
import ru.content.management.dto.PodcastUpdateDTO;
import java.util.List;

public interface PodcastService {
    List<PodcastDTO> find(String title);
    PodcastDTO findById(Long id);
    PodcastDTO create(PodcastCreateDTO dto);
    PodcastDTO update(Long id, PodcastUpdateDTO dto);
    void delete(Long id);

    List<PodcastEpisodeDTO> findEpisodesByPodcastId(Long podcastId, Integer episodeNumber, String title);
    PodcastEpisodeDTO addEpisode(Long podcastId, PodcastEpisodeCreateDTO dto);
    PodcastEpisodeDTO findEpisodeById(Long episodeId);
    PodcastEpisodeDTO updateEpisode(Long episodeId, PodcastEpisodeUpdateDTO dto);
    void deleteEpisode(Long episodeId);
}
