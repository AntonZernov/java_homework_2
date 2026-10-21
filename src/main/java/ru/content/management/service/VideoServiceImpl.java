package ru.content.management.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.content.management.dto.VideoCreateDTO;
import ru.content.management.dto.VideoDTO;
import ru.content.management.dto.VideoUpdateDTO;
import ru.content.management.entity.Video;
import ru.content.management.exception.EntityNotFoundException;
import ru.content.management.mapper.VideoMapper;
import ru.content.management.repository.VideoRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {

    private final VideoRepository videoRepository;
    private final VideoMapper videoMapper;

    @Override
    public List<VideoDTO> findAll() {
        return videoRepository.findAll().stream()
                .map(videoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public VideoDTO findById(Long id) {
        return videoRepository.findById(id)
                .map(videoMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Видео не найдено"));
    }

    @Override
    public VideoDTO create(VideoCreateDTO dto) {
        Video video = videoMapper.toEntityCreate(dto);
        System.out.println("Video: " + video);
        return videoMapper.toDTO(videoRepository.save(video));
    }

    @Override
    public VideoDTO update(Long id, VideoUpdateDTO dto) {
        Video existing = videoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Видео для редактирования не найдено"));
        videoMapper.updateEntity(dto, existing);
        return videoMapper.toDTO(videoRepository.save(existing));
    }

    @Override
    public void delete(Long id) {
        if (!videoRepository.existsById(id)) {
            throw new EntityNotFoundException("Видео для удаления не найдено");
        }
        videoRepository.deleteById(id);
    }
}
