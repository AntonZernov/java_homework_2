package ru.content.management.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.content.management.dto.PodcastCreateDTO;
import ru.content.management.dto.PodcastDTO;
import ru.content.management.dto.PodcastEpisodeCreateDTO;
import ru.content.management.dto.PodcastEpisodeDTO;
import ru.content.management.dto.PodcastEpisodeUpdateDTO;
import ru.content.management.dto.PodcastUpdateDTO;
import ru.content.management.entity.Podcast;
import ru.content.management.entity.PodcastEpisode;
import ru.content.management.exception.EntityNotFoundException;
import ru.content.management.repository.PodcastEpisodeRepository;
import ru.content.management.repository.PodcastRepository;
import ru.content.management.utils.PodcastEpisodeMapper;
import ru.content.management.utils.PodcastMapper;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class PodcastServiceImplTest {

    @Mock
    private PodcastRepository podcastRepository;

    @Mock
    private PodcastEpisodeRepository podcastEpisodeRepository;

    @InjectMocks
    private PodcastServiceImpl podcastService;

    @Test
    @SuppressWarnings("unchecked")
    void findSuccessTest() {
        Podcast podcast = createPodcast();
        PodcastDTO dto = createPodcastDTO();

        when(podcastRepository.findAll((Specification<Podcast>) any())).thenReturn(List.of(podcast));
        try (MockedStatic<PodcastMapper> mockedMapper = Mockito.mockStatic(PodcastMapper.class)) {
            mockedMapper.when(() -> PodcastMapper.toDTO(podcast)).thenReturn(dto);

            List<PodcastDTO> result = podcastService.find(null);

            assertEquals(1, result.size());
            assertEquals("Test Title", result.getFirst().getTitle());
            verify(podcastRepository).findAll((Specification<Podcast>) any());
        }
    }

    private Podcast createPodcast() {
        Podcast podcast = new Podcast();
        podcast.setId(1L);
        podcast.setTitle("Test Title");
        return podcast;
    }

    private PodcastDTO createPodcastDTO() {
        PodcastDTO dto = new PodcastDTO();
        dto.setId(1L);
        dto.setTitle("Test Title");
        return dto;
    }

    @Test
    @SuppressWarnings("unchecked")
    void findSuccessEmptyTest() {
        when(podcastRepository.findAll((Specification<Podcast>) any())).thenReturn(List.of());

        List<PodcastDTO> result = podcastService.find(null);

        assertTrue(result.isEmpty());
        verify(podcastRepository).findAll((Specification<Podcast>) any());
    }

    @Test
    void findByIdSuccessTest() {
        Podcast podcast = createPodcast();
        PodcastDTO dto = createPodcastDTO();

        when(podcastRepository.findById(1L)).thenReturn(java.util.Optional.of(podcast));
        try (MockedStatic<PodcastMapper> mockedMapper = Mockito.mockStatic(PodcastMapper.class)) {
            mockedMapper.when(() -> PodcastMapper.toDTO(podcast)).thenReturn(dto);
            PodcastDTO result = podcastService.findById(1L);
            assertNotNull(result);
            assertEquals("Test Title", result.getTitle());
            verify(podcastRepository).findById(1L);
        }
    }

    @Test
    void findByIdNotFoundExceptionTest() {
        when(podcastRepository.findById(99L)).thenReturn(java.util.Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            podcastService.findById(99L);
        });

        assertEquals("Подкаст не найден", exception.getMessage());
        verify(podcastRepository).findById(99L);
    }

    @Test
    void createSuccessTest() {
        PodcastCreateDTO createDTO = new PodcastCreateDTO();
        createDTO.setTitle("New Title");

        Podcast entity = new Podcast();
        entity.setTitle("New Title");

        PodcastDTO savedDTO = new PodcastDTO();
        savedDTO.setId(1L);
        savedDTO.setTitle("New Title");
        try (MockedStatic<PodcastMapper> mockedMapper = Mockito.mockStatic(PodcastMapper.class)) {
            mockedMapper.when(() -> PodcastMapper.toEntityCreate(createDTO)).thenReturn(entity);
            when(podcastRepository.save(entity)).thenReturn(entity);
            when(PodcastMapper.toDTO(entity)).thenReturn(savedDTO);

            PodcastDTO result = podcastService.create(createDTO);

            assertNotNull(result);
            assertEquals(1L, result.getId());
            verify(podcastRepository).save(entity);
        }
    }

    @Test
    void updateAllFieldsSuccessTest() {
        PodcastUpdateDTO updateDTO = new PodcastUpdateDTO();
        updateDTO.setTitle("Updated Title");

        Podcast existing = createPodcast();

        Podcast saved = new Podcast();
        saved.setId(1L);
        saved.setTitle("Updated Title");

        PodcastDTO savedDTO = new PodcastDTO();
        savedDTO.setId(1L);
        savedDTO.setTitle("Updated Title");

        when(podcastRepository.findById(1L)).thenReturn(java.util.Optional.of(existing));
        when(podcastRepository.save(existing)).thenReturn(saved);
        try (MockedStatic<PodcastMapper> mockedMapper = Mockito.mockStatic(PodcastMapper.class)) {
            mockedMapper.when(() -> PodcastMapper.toDTO(saved)).thenReturn(savedDTO);

            PodcastDTO result = podcastService.update(1L, updateDTO);

            assertNotNull(result);
            assertEquals("Updated Title", result.getTitle());
            verify(podcastRepository).findById(1L);
            verify(podcastRepository).save(existing);
        }
    }

    @Test
    void updateNotFoundExceptionTest() {
        PodcastUpdateDTO updateDTO = new PodcastUpdateDTO();
        updateDTO.setTitle("Updated Title");

        when(podcastRepository.findById(99L)).thenReturn(java.util.Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            podcastService.update(99L, updateDTO);
        });

        assertEquals("Подкаст для редактирования не найден", exception.getMessage());
        verify(podcastRepository).findById(99L);
    }

    @Test
    void deleteSuccessTest() {
        when(podcastRepository.existsById(1L)).thenReturn(true);

        podcastService.delete(1L);

        verify(podcastRepository).existsById(1L);
        verify(podcastRepository).deleteById(1L);
    }

    @Test
    void deleteNotFoundExceptionTest() {
        when(podcastRepository.existsById(99L)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            podcastService.delete(99L);
        });

        assertEquals("Подкаст для удаления не найден", exception.getMessage());
        verify(podcastRepository).existsById(99L);
        verify(podcastRepository, never()).deleteById(anyLong());
    }

    @Test
    @SuppressWarnings("unchecked")
    void findEpisodesByPodcastIdSuccessTest() {
        PodcastEpisode episode = createPodcastEpisode();
        PodcastEpisodeDTO dto = createPodcastEpisodeDTO();

        when(podcastEpisodeRepository.findAll(any(Specification.class))).thenReturn(List.of(episode));
        try (MockedStatic<PodcastEpisodeMapper> mockedMapper = Mockito.mockStatic(PodcastEpisodeMapper.class)) {
            mockedMapper.when(() -> PodcastEpisodeMapper.toDTO(episode)).thenReturn(dto);

            List<PodcastEpisodeDTO> result = podcastService.findEpisodesByPodcastId(1L, null, null);

            assertEquals(1, result.size());
            assertEquals("Episode 1", result.getFirst().getTitle());
            verify(podcastEpisodeRepository).findAll(any(Specification.class));
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    void findEpisodesByPodcastIdWithEpisodeNumberTest() {
        PodcastEpisode episode = createPodcastEpisode();
        PodcastEpisodeDTO dto = createPodcastEpisodeDTO();

        when(podcastEpisodeRepository.findAll(any(Specification.class))).thenReturn(List.of(episode));
        try (MockedStatic<PodcastEpisodeMapper> mockedMapper = Mockito.mockStatic(PodcastEpisodeMapper.class)) {
            mockedMapper.when(() -> PodcastEpisodeMapper.toDTO(episode)).thenReturn(dto);

            List<PodcastEpisodeDTO> result = podcastService.findEpisodesByPodcastId(1L, 1, null);

            assertEquals(1, result.size());
            verify(podcastEpisodeRepository).findAll(any(Specification.class));
        }
    }

    @Test
    void addEpisodeSuccessTest() {
        PodcastEpisodeCreateDTO createDTO = new PodcastEpisodeCreateDTO();
        createDTO.setEpisodeNumber(1);
        createDTO.setTitle("New Episode");
        createDTO.setAudioUrl("http://example.com/audio.mp3");
        createDTO.setDuration(LocalTime.of(0, 4, 24));

        Podcast podcast = createPodcast();
        PodcastEpisode episode = new PodcastEpisode();
        episode.setPodcastId(1L);
        episode.setEpisodeNumber(1);
        episode.setTitle("New Episode");
        episode.setAudioUrl("http://example.com/audio.mp3");
        episode.setDuration(LocalTime.of(0, 4, 24));

        PodcastEpisodeDTO savedDTO = new PodcastEpisodeDTO();
        savedDTO.setId(1L);
        savedDTO.setPodcastId(1L);
        savedDTO.setEpisodeNumber(1);
        savedDTO.setTitle("New Episode");
        savedDTO.setAudioUrl("http://example.com/audio.mp3");
        savedDTO.setDuration(LocalTime.of(0, 4, 24));

        when(podcastRepository.existsById(1L)).thenReturn(true);
        when(podcastEpisodeRepository.save(episode)).thenReturn(episode);
        try (MockedStatic<PodcastEpisodeMapper> mockedMapper = Mockito.mockStatic(PodcastEpisodeMapper.class)) {
            mockedMapper.when(() -> PodcastEpisodeMapper.toEntityCreate(createDTO)).thenReturn(episode);
            mockedMapper.when(() -> PodcastEpisodeMapper.toDTO(episode)).thenReturn(savedDTO);

            PodcastEpisodeDTO result = podcastService.addEpisode(1L, createDTO);

            assertNotNull(result);
            assertEquals(1L, result.getId());
            verify(podcastRepository).existsById(1L);
            verify(podcastEpisodeRepository).save(episode);
        }
    }

    @Test
    void addEpisodePodcastNotFoundTest() {
        PodcastEpisodeCreateDTO createDTO = new PodcastEpisodeCreateDTO();
        createDTO.setEpisodeNumber(1);
        createDTO.setTitle("New Episode");
        createDTO.setAudioUrl("http://example.com/audio.mp3");
        createDTO.setDuration(LocalTime.of(0, 4, 24));

        when(podcastRepository.existsById(99L)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            podcastService.addEpisode(99L, createDTO);
        });

        assertEquals("Подкаст не найден", exception.getMessage());
        verify(podcastRepository).existsById(99L);
        verify(podcastEpisodeRepository, never()).save(any());
    }

    @Test
    void findEpisodeByIdSuccessTest() {
        PodcastEpisode episode = createPodcastEpisode();
        PodcastEpisodeDTO dto = createPodcastEpisodeDTO();

        when(podcastEpisodeRepository.findById(1L)).thenReturn(java.util.Optional.of(episode));
        try (MockedStatic<PodcastEpisodeMapper> mockedMapper = Mockito.mockStatic(PodcastEpisodeMapper.class)) {
            mockedMapper.when(() -> PodcastEpisodeMapper.toDTO(episode)).thenReturn(dto);
            PodcastEpisodeDTO result = podcastService.findEpisodeById(1L);
            assertNotNull(result);
            assertEquals("Episode 1", result.getTitle());
            verify(podcastEpisodeRepository).findById(1L);
        }
    }

    @Test
    void findEpisodeByIdNotFoundExceptionTest() {
        when(podcastEpisodeRepository.findById(99L)).thenReturn(java.util.Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            podcastService.findEpisodeById(99L);
        });

        assertEquals("Эпизод не найден", exception.getMessage());
        verify(podcastEpisodeRepository).findById(99L);
    }

    @Test
    void updateEpisodeSuccessTest() {
        PodcastEpisodeUpdateDTO updateDTO = new PodcastEpisodeUpdateDTO();
        updateDTO.setTitle("Updated Episode");

        PodcastEpisode existing = createPodcastEpisode();

        PodcastEpisode saved = new PodcastEpisode();
        saved.setId(1L);
        saved.setPodcastId(1L);
        saved.setEpisodeNumber(1);
        saved.setTitle("Updated Episode");
        saved.setAudioUrl("http://example.com/audio.mp3");
        saved.setDuration(LocalTime.of(0, 4, 24));

        PodcastEpisodeDTO savedDTO = new PodcastEpisodeDTO();
        savedDTO.setId(1L);
        savedDTO.setPodcastId(1L);
        savedDTO.setEpisodeNumber(1);
        savedDTO.setTitle("Updated Episode");
        savedDTO.setAudioUrl("http://example.com/audio.mp3");
        savedDTO.setDuration(LocalTime.of(0, 4, 24));

        when(podcastEpisodeRepository.findById(1L)).thenReturn(java.util.Optional.of(existing));
        when(podcastEpisodeRepository.save(existing)).thenReturn(saved);
        try (MockedStatic<PodcastEpisodeMapper> mockedMapper = Mockito.mockStatic(PodcastEpisodeMapper.class)) {
            mockedMapper.when(() -> PodcastEpisodeMapper.toDTO(saved)).thenReturn(savedDTO);

            PodcastEpisodeDTO result = podcastService.updateEpisode(1L, updateDTO);

            assertNotNull(result);
            assertEquals("Updated Episode", result.getTitle());
            verify(podcastEpisodeRepository).findById(1L);
            verify(podcastEpisodeRepository).save(existing);
        }
    }

    @Test
    void updateEpisodeNotFoundExceptionTest() {
        PodcastEpisodeUpdateDTO updateDTO = new PodcastEpisodeUpdateDTO();
        updateDTO.setTitle("Updated Episode");

        when(podcastEpisodeRepository.findById(99L)).thenReturn(java.util.Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            podcastService.updateEpisode(99L, updateDTO);
        });

        assertEquals("Эпизод для редактирования не найден", exception.getMessage());
        verify(podcastEpisodeRepository).findById(99L);
    }

    @Test
    void deleteEpisodeSuccessTest() {
        when(podcastEpisodeRepository.existsById(1L)).thenReturn(true);

        podcastService.deleteEpisode(1L);

        verify(podcastEpisodeRepository).existsById(1L);
        verify(podcastEpisodeRepository).deleteById(1L);
    }

    @Test
    void deleteEpisodeNotFoundExceptionTest() {
        when(podcastEpisodeRepository.existsById(99L)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            podcastService.deleteEpisode(99L);
        });

        assertEquals("Эпизод для удаления не найден", exception.getMessage());
        verify(podcastEpisodeRepository).existsById(99L);
        verify(podcastEpisodeRepository, never()).deleteById(anyLong());
    }

    private PodcastEpisode createPodcastEpisode() {
        PodcastEpisode episode = new PodcastEpisode();
        episode.setId(1L);
        episode.setPodcastId(1L);
        episode.setEpisodeNumber(1);
        episode.setTitle("Episode 1");
        episode.setAudioUrl("http://example.com/audio.mp3");
        episode.setDuration(LocalTime.of(0, 4, 24));
        return episode;
    }

    private PodcastEpisodeDTO createPodcastEpisodeDTO() {
        PodcastEpisodeDTO dto = new PodcastEpisodeDTO();
        dto.setId(1L);
        dto.setPodcastId(1L);
        dto.setEpisodeNumber(1);
        dto.setTitle("Episode 1");
        dto.setAudioUrl("http://example.com/audio.mp3");
        dto.setDuration(LocalTime.of(0, 4, 24));
        return dto;
    }
}
