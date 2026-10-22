package ru.content.management.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.content.management.dto.PodcastCreateDTO;
import ru.content.management.dto.PodcastDTO;
import ru.content.management.dto.PodcastEpisodeCreateDTO;
import ru.content.management.dto.PodcastEpisodeDTO;
import ru.content.management.dto.PodcastUpdateDTO;
import ru.content.management.entity.Podcast;
import ru.content.management.entity.PodcastEpisode;
import ru.content.management.exception.EntityNotFoundException;
import ru.content.management.repository.PodcastEpisodeRepository;
import ru.content.management.repository.PodcastRepository;
import ru.content.management.utils.PodcastEpisodeMapper;
import ru.content.management.utils.PodcastMapper;
import ru.content.management.utils.SpecificationUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PodcastServiceImpl implements PodcastService {

    private final PodcastRepository podcastRepository;
    private final PodcastEpisodeRepository podcastEpisodeRepository;

    @Override
    public List<PodcastDTO> find(String title) {
        Specification<Podcast> spec = Specification.where(null);

        if (StringUtils.hasText(title)) {
            spec = spec.and(SpecificationUtils.buildLikeSpec(title, "title"));
        }

        return podcastRepository.findAll(spec).stream()
                .map(PodcastMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PodcastDTO findById(Long id) {
        return podcastRepository.findById(id)
                .map(PodcastMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Подкаст не найден"));
    }

    @Override
    public PodcastDTO create(PodcastCreateDTO dto) {
        Podcast podcast = PodcastMapper.toEntityCreate(dto);
        return PodcastMapper.toDTO(podcastRepository.save(podcast));
    }

    @Override
    public PodcastDTO update(Long id, PodcastUpdateDTO dto) {
        Podcast existing = podcastRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Подкаст для редактирования не найден"));
        PodcastMapper.updateEntity(dto, existing);
        return PodcastMapper.toDTO(podcastRepository.save(existing));
    }

    @Override
    public void delete(Long id) {
        if (!podcastRepository.existsById(id)) {
            throw new EntityNotFoundException("Подкаст для удаления не найден");
        }
        podcastRepository.deleteById(id);
    }

    @Override
    public List<PodcastEpisodeDTO> findEpisodesByPodcastId(Long podcastId, Integer episodeNumber, String title) {
        Specification<PodcastEpisode> spec = Specification.where(
                (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("podcastId"), podcastId)
        );

        if (episodeNumber != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("episodeNumber"), episodeNumber));
        }

        if (StringUtils.hasText(title)) {
            spec = spec.and(SpecificationUtils.buildLikeSpec(title, "title"));
        }

        return podcastEpisodeRepository.findAll(spec).stream()
                .map(PodcastEpisodeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PodcastEpisodeDTO addEpisode(Long podcastId, PodcastEpisodeCreateDTO dto) {
        if (!podcastRepository.existsById(podcastId)) {
            throw new EntityNotFoundException("Подкаст не найден");
        }

        PodcastEpisode episode = PodcastEpisodeMapper.toEntityCreate(dto);
        episode.setPodcastId(podcastId);

        return PodcastEpisodeMapper.toDTO(podcastEpisodeRepository.save(episode));
    }
}
