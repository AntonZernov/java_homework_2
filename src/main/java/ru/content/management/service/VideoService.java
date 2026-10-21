package ru.content.management.service;

import ru.content.management.dto.VideoDTO;
import ru.content.management.dto.VideoCreateDTO;
import ru.content.management.dto.VideoUpdateDTO;
import java.util.List;

public interface VideoService {
    List<VideoDTO> findAll();
    List<VideoDTO> find(String title, String url, String durationFrom, String durationTo);
    VideoDTO findById(Long id);
    VideoDTO create(VideoCreateDTO dto);
    VideoDTO update(Long id, VideoUpdateDTO dto);
    void delete(Long id);
}
