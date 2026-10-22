package ru.content.management.utils;

import lombok.experimental.UtilityClass;
import ru.content.management.dto.PodcastEpisodeCreateDTO;
import ru.content.management.dto.PodcastEpisodeDTO;
import ru.content.management.entity.PodcastEpisode;

@UtilityClass
public class PodcastEpisodeMapper {

    public PodcastEpisode toEntity(PodcastEpisodeDTO dto) {
        PodcastEpisode episode = new PodcastEpisode();
        episode.setId(dto.getId());
        episode.setPodcastId(dto.getPodcastId());
        episode.setEpisodeNumber(dto.getEpisodeNumber());
        episode.setTitle(dto.getTitle());
        episode.setAudioUrl(dto.getAudioUrl());
        episode.setDuration(dto.getDuration());
        return episode;
    }

    public PodcastEpisode toEntityCreate(PodcastEpisodeCreateDTO dto) {
        PodcastEpisode episode = new PodcastEpisode();
        episode.setEpisodeNumber(dto.getEpisodeNumber());
        episode.setTitle(dto.getTitle());
        episode.setAudioUrl(dto.getAudioUrl());
        episode.setDuration(dto.getDuration());
        return episode;
    }

    public PodcastEpisodeDTO toDTO(PodcastEpisode episode) {
        PodcastEpisodeDTO dto = new PodcastEpisodeDTO();
        dto.setId(episode.getId());
        dto.setPodcastId(episode.getPodcastId());
        dto.setEpisodeNumber(episode.getEpisodeNumber());
        dto.setTitle(episode.getTitle());
        dto.setAudioUrl(episode.getAudioUrl());
        dto.setDuration(episode.getDuration());
        return dto;
    }
}
