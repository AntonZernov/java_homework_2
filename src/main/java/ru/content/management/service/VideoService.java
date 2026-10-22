package ru.content.management.service;

import ru.content.management.dto.VideoDTO;
import ru.content.management.dto.VideoCreateDTO;
import ru.content.management.dto.VideoUpdateDTO;

import java.time.LocalTime;
import java.util.List;

public interface VideoService {
    List<VideoDTO> find(String title, String url, LocalTime durationFrom, LocalTime durationTo);
    VideoDTO findById(Long id);
    VideoDTO create(VideoCreateDTO dto);
    VideoDTO update(Long id, VideoUpdateDTO dto);
    void delete(Long id);
}
