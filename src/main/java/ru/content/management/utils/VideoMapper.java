package ru.content.management.utils;

import lombok.experimental.UtilityClass;
import ru.content.management.dto.VideoCreateDTO;
import ru.content.management.dto.VideoDTO;
import ru.content.management.dto.VideoUpdateDTO;
import ru.content.management.entity.Video;

@UtilityClass
public class VideoMapper {

    public Video toEntityCreate(VideoCreateDTO dto) {
        Video video = new Video();
        video.setTitle(dto.getTitle());
        video.setUrl(dto.getUrl());
        video.setDuration(dto.getDuration());
        return video;
    }

    public void updateEntity(VideoUpdateDTO dto, Video video) {
        if (dto.getTitle() != null) video.setTitle(dto.getTitle());
        if (dto.getUrl() != null) video.setUrl(dto.getUrl());
        if (dto.getDuration() != null) video.setDuration(dto.getDuration());
    }

    public VideoDTO toDTO(Video video) {
        VideoDTO dto = new VideoDTO();
        dto.setId(video.getId());
        dto.setTitle(video.getTitle());
        dto.setUrl(video.getUrl());
        dto.setDuration(video.getDuration());
        return dto;
    }
}
