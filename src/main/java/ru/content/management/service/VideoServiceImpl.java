package ru.content.management.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import ru.content.management.dto.VideoCreateDTO;
import ru.content.management.dto.VideoDTO;
import ru.content.management.dto.VideoUpdateDTO;
import ru.content.management.entity.Video;
import ru.content.management.exception.EntityNotFoundException;
import ru.content.management.utils.SpecificationUtils;
import ru.content.management.utils.VideoMapper;
import ru.content.management.repository.VideoRepository;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {

    private final VideoRepository videoRepository;

    @Override
    public List<VideoDTO> find(String title, String url, LocalTime durationFrom, LocalTime durationTo) {
        Specification<Video> spec = Specification.where(null);

        if (StringUtils.hasText(title)) {
            spec = spec.and(SpecificationUtils.buildLikeSpec(title, "title"));
        }

        if (StringUtils.hasText(url)) {
            spec = spec.and(SpecificationUtils.buildLikeSpec(url, "url"));
        }

        if (durationFrom != null || durationTo != null) {
            spec = spec.and(SpecificationUtils.buildDurationSpec(durationFrom, durationTo, "duration"));
        }

        return videoRepository.findAll(spec).stream()
                .map(VideoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public VideoDTO findById(Long id) {
        return videoRepository.findById(id)
                .map(VideoMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Видео не найдено"));
    }

    @Override
    public VideoDTO create(VideoCreateDTO dto) {
        Video video = VideoMapper.toEntityCreate(dto);
        return VideoMapper.toDTO(videoRepository.save(video));
    }

    @Override
    public VideoDTO update(Long id, VideoUpdateDTO dto) {
        Video existing = videoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Видео для редактирования не найдено"));
        VideoMapper.updateEntity(dto, existing);
        return VideoMapper.toDTO(videoRepository.save(existing));
    }

    @Override
    public void delete(Long id) {
        if (!videoRepository.existsById(id)) {
            throw new EntityNotFoundException("Видео для удаления не найдено");
        }
        videoRepository.deleteById(id);
    }
}
