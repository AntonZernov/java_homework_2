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
import ru.content.management.mapper.VideoMapper;
import ru.content.management.repository.VideoRepository;
import java.time.LocalTime;
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
    public List<VideoDTO> find(String title, String url, String durationFrom, String durationTo) {
        Specification<Video> spec = Specification.where(null);

        if (StringUtils.hasText(title)) {
            spec = spec.and(buildTitleSpec(title));
        }

        if (StringUtils.hasText(url)) {
            spec = spec.and(buildUrlSpec(url));
        }

        if (StringUtils.hasText(durationFrom) || StringUtils.hasText(durationTo)) {
            spec = spec.and(buildDurationSpec(durationFrom, durationTo));
        }

        return videoRepository.findAll(spec).stream()
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

    private Specification<Video> buildTitleSpec(String title) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    private Specification<Video> buildUrlSpec(String url) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("url")), "%" + url.toLowerCase() + "%");
    }

    private Specification<Video> buildDurationSpec(String durationFrom, String durationTo) {
        if (StringUtils.hasText(durationFrom) && StringUtils.hasText(durationTo)) {
            if (durationFrom.equals(durationTo)) {
                LocalTime time = LocalTime.parse(durationFrom);
                return (root, query, cb) -> cb.equal(root.get("duration"), time);
            }
            LocalTime from = LocalTime.parse(durationFrom);
            LocalTime to = LocalTime.parse(durationTo);
            return (root, query, cb) -> cb.and(
                    cb.greaterThanOrEqualTo(root.get("duration"), from),
                    cb.lessThanOrEqualTo(root.get("duration"), to)
            );
        }
        if (StringUtils.hasText(durationFrom)) {
            LocalTime from = LocalTime.parse(durationFrom);
            return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("duration"), from);
        }
        LocalTime to = LocalTime.parse(durationTo);
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("duration"), to);
    }
}
