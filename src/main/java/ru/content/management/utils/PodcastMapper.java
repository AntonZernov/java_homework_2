package ru.content.management.utils;

import lombok.experimental.UtilityClass;
import ru.content.management.dto.PodcastCreateDTO;
import ru.content.management.dto.PodcastDTO;
import ru.content.management.dto.PodcastUpdateDTO;
import ru.content.management.entity.Podcast;

@UtilityClass
public class PodcastMapper {

    public Podcast toEntityCreate(PodcastCreateDTO dto) {
        Podcast podcast = new Podcast();
        podcast.setTitle(dto.getTitle());
        return podcast;
    }

    public void updateEntity(PodcastUpdateDTO dto, Podcast podcast) {
        if (dto.getTitle() != null) podcast.setTitle(dto.getTitle());
    }

    public PodcastDTO toDTO(Podcast podcast) {
        PodcastDTO dto = new PodcastDTO();
        dto.setId(podcast.getId());
        dto.setTitle(podcast.getTitle());
        return dto;
    }
}
