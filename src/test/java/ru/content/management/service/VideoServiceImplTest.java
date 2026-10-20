package ru.content.management.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import ru.content.management.dto.VideoCreateDTO;
import ru.content.management.dto.VideoDTO;
import ru.content.management.dto.VideoUpdateDTO;
import ru.content.management.entity.Video;
import ru.content.management.exception.EntityNotFoundException;
import ru.content.management.repository.VideoRepository;
import ru.content.management.utils.VideoMapper;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VideoServiceImplTest {

    @Mock
    private VideoRepository videoRepository;
    @InjectMocks
    private VideoServiceImpl videoService;

    private Video createVideo() {
        Video video = new Video();
        video.setId(1L);
        video.setTitle("Test Title");
        video.setUrl("https://example.com/video");
        video.setDuration(LocalTime.of(0, 5, 30));
        return video;
    }

    private VideoDTO createVideoDTO() {
        VideoDTO dto = new VideoDTO();
        dto.setId(1L);
        dto.setTitle("Test Title");
        dto.setUrl("https://example.com/video");
        dto.setDuration(LocalTime.of(0, 5, 30));
        return dto;
    }

    @Test
    @SuppressWarnings("unchecked")
    void findSuccessAllTest() {
        Video video = createVideo();
        VideoDTO dto = createVideoDTO();

        when(videoRepository.findAll((Specification<Video>) any())).thenReturn(List.of(video));
        try (MockedStatic<VideoMapper> mockedMapper = mockStatic(VideoMapper.class)) {
            mockedMapper.when(() -> VideoMapper.toDTO(video)).thenReturn(dto);

            List<VideoDTO> result = videoService.find(null, null, null, null);

            assertEquals(1, result.size());
            assertEquals("Test Title", result.getFirst().getTitle());
            verify(videoRepository).findAll((Specification<Video>) any());
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    void findSuccessEmptyTest() {
        when(videoRepository.findAll((Specification<Video>) any())).thenReturn(List.of());

        List<VideoDTO> result = videoService.find(null, null, null, null);

        assertTrue(result.isEmpty());
        verify(videoRepository).findAll((Specification<Video>)any());
    }

    @Test
    void findByIdSuccessTest() {
        Video video = createVideo();
        VideoDTO dto = createVideoDTO();

        when(videoRepository.findById(1L)).thenReturn(java.util.Optional.of(video));
        try (MockedStatic<VideoMapper> mockedMapper = mockStatic(VideoMapper.class)) {
            mockedMapper.when(() -> VideoMapper.toDTO(video)).thenReturn(dto);

            VideoDTO result = videoService.findById(1L);

            assertNotNull(result);
            assertEquals("Test Title", result.getTitle());
            verify(videoRepository).findById(1L);
        }
    }

    @Test
    void findByIdNotFoundExceptionTest() {
        when(videoRepository.findById(99L)).thenReturn(java.util.Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            videoService.findById(99L);
        });

        assertEquals("Видео не найдено", exception.getMessage());
        verify(videoRepository).findById(99L);
    }

    @Test
    void createSuccessTest() {
        VideoCreateDTO createDTO = new VideoCreateDTO();
        createDTO.setTitle("New Title");
        createDTO.setUrl("https://example.com/new");
        createDTO.setDuration(LocalTime.of(0, 10, 0));

        Video entity = new Video();
        entity.setTitle("New Title");
        entity.setUrl("https://example.com/new");
        entity.setDuration(LocalTime.of(0, 10, 0));

        VideoDTO savedDTO = new VideoDTO();
        savedDTO.setId(1L);
        savedDTO.setTitle("New Title");
        savedDTO.setUrl("https://example.com/new");
        savedDTO.setDuration(LocalTime.of(0, 10, 0));
        try (MockedStatic<VideoMapper> mockedMapper = mockStatic(VideoMapper.class)) {
            mockedMapper.when(() -> VideoMapper.toEntityCreate(createDTO)).thenReturn(entity);
            when(videoRepository.save(entity)).thenReturn(entity);
            when(VideoMapper.toDTO(entity)).thenReturn(savedDTO);

            VideoDTO result = videoService.create(createDTO);

            assertNotNull(result);
            assertEquals(1L, result.getId());
            verify(videoRepository).save(entity);
        }
    }

    @Test
    void updateAllFieldsSuccessTest() {
        VideoUpdateDTO updateDTO = new VideoUpdateDTO();
        updateDTO.setTitle("Updated Title");
        updateDTO.setUrl("https://example.com/updated");

        Video existing = createVideo();

        Video saved = new Video();
        saved.setId(1L);
        saved.setTitle("Updated Title");
        saved.setUrl("https://example.com/updated");
        saved.setDuration(LocalTime.of(0, 5, 30));

        VideoDTO savedDTO = new VideoDTO();
        savedDTO.setId(1L);
        savedDTO.setTitle("Updated Title");
        savedDTO.setUrl("https://example.com/updated");
        savedDTO.setDuration(LocalTime.of(0, 5, 30));

        when(videoRepository.findById(1L)).thenReturn(java.util.Optional.of(existing));
        when(videoRepository.save(existing)).thenReturn(saved);
        try (MockedStatic<VideoMapper> mockedMapper = mockStatic(VideoMapper.class)) {
            mockedMapper.when(() -> VideoMapper.toDTO(saved)).thenReturn(savedDTO);

            VideoDTO result = videoService.update(1L, updateDTO);

            assertNotNull(result);
            assertEquals("Updated Title", result.getTitle());
            assertEquals("https://example.com/updated", result.getUrl());
            verify(videoRepository).findById(1L);
            verify(videoRepository).save(existing);
        }

    }

    @Test
    void updateNotFoundExceptionTest() {
        VideoUpdateDTO updateDTO = new VideoUpdateDTO();
        updateDTO.setTitle("Updated Title");

        when(videoRepository.findById(99L)).thenReturn(java.util.Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            videoService.update(99L, updateDTO);
        });

        assertEquals("Видео для редактирования не найдено", exception.getMessage());
        verify(videoRepository).findById(99L);
    }

    @Test
    void updateNonNullFieldsSuccessTest() {
        VideoUpdateDTO updateDTO = new VideoUpdateDTO();
        updateDTO.setTitle("Updated Title");

        Video existing = createVideo();

        Video saved = new Video();
        saved.setId(1L);
        saved.setTitle("Updated Title");
        saved.setUrl("https://example.com/video");
        saved.setDuration(LocalTime.of(0, 5, 30));

        VideoDTO savedDTO = new VideoDTO();
        savedDTO.setId(1L);
        savedDTO.setTitle("Updated Title");
        savedDTO.setUrl("https://example.com/video");
        savedDTO.setDuration(LocalTime.of(0, 5, 30));

        when(videoRepository.findById(1L)).thenReturn(java.util.Optional.of(existing));
        when(videoRepository.save(existing)).thenReturn(saved);

        try (MockedStatic<VideoMapper> mockedMapper = mockStatic(VideoMapper.class)) {
            mockedMapper.when(() -> VideoMapper.toDTO(saved)).thenReturn(savedDTO);

            VideoDTO result = videoService.update(1L, updateDTO);

            assertNotNull(result);
            assertEquals("Updated Title", result.getTitle());
            assertEquals("https://example.com/video", result.getUrl());
        }
    }

    @Test
    void deleteSuccessTest() {
        when(videoRepository.existsById(1L)).thenReturn(true);

        videoService.delete(1L);

        verify(videoRepository).existsById(1L);
        verify(videoRepository).deleteById(1L);
    }

    @Test
    void deleteNotFoundExceptionTest() {
        when(videoRepository.existsById(99L)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            videoService.delete(99L);
        });

        assertEquals("Видео для удаления не найдено", exception.getMessage());
        verify(videoRepository).existsById(99L);
        verify(videoRepository, never()).deleteById(anyLong());
    }
}
