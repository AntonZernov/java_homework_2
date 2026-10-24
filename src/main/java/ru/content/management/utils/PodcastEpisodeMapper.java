package ru.content.management.utils;

import lombok.experimental.UtilityClass;
import ru.content.management.dto.PodcastEpisodeCreateDTO;
import ru.content.management.dto.PodcastEpisodeDTO;
import ru.content.management.dto.PodcastEpisodeUpdateDTO;
import ru.content.management.entity.PodcastEpisode;

@UtilityClass
public class PodcastEpisodeMapper {

    public PodcastEpisode toEntityCreate(PodcastEpisodeCreateDTO dto) {
        PodcastEpisode episode = new PodcastEpisode();
        episode.setEpisodeNumber(dto.getEpisodeNumber());
        episode.setTitle(dto.getTitle());
        episode.setAudioUrl(dto.getAudioUrl());
        episode.setDuration(dto.getDuration());
        return episode;
    }

    public void updateEntity(PodcastEpisodeUpdateDTO dto, PodcastEpisode episode) {
        if (dto.getTitle() != null) episode.setTitle(dto.getTitle());
        if (dto.getAudioUrl() != null) episode.setAudioUrl(dto.getAudioUrl());
        if (dto.getDuration() != null) episode.setDuration(dto.getDuration());
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
