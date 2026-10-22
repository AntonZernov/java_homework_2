package ru.content.management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.content.management.dto.PodcastCreateDTO;
import ru.content.management.dto.PodcastDTO;
import ru.content.management.dto.PodcastEpisodeCreateDTO;
import ru.content.management.dto.PodcastEpisodeDTO;
import ru.content.management.dto.PodcastUpdateDTO;
import ru.content.management.exception.EntityNotFoundException;
import ru.content.management.exception.GlobalExceptionHandler;
import ru.content.management.service.PodcastService;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PodcastController.class)
@Import(GlobalExceptionHandler.class)
class PodcastControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PodcastService podcastService;

    @Test
    void findAllSuccessTest() throws Exception {
        PodcastDTO podcast = new PodcastDTO();
        given(podcastService.find(null)).willReturn(List.of(podcast));

        mockMvc.perform(get("/podcasts/find"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void findByIdSuccessTest() throws Exception {
        PodcastDTO podcast = new PodcastDTO();
        given(podcastService.findById(1L)).willReturn(podcast);

        mockMvc.perform(get("/podcasts/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void findByIdNotFoundTest() throws Exception {
        given(podcastService.findById(1L))
                .willThrow(new EntityNotFoundException("Подкаст не найден"));

        mockMvc.perform(get("/podcasts/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Подкаст не найден"));
    }

    @Test
    void createSuccessTest() throws Exception {
        PodcastCreateDTO createDTO = new PodcastCreateDTO();
        createDTO.setTitle("Valid Title");

        PodcastDTO responseDTO = new PodcastDTO();
        given(podcastService.create(any(PodcastCreateDTO.class))).willReturn(responseDTO);

        mockMvc.perform(post("/podcasts/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void createBlankFieldsValidationTest() throws Exception {
        PodcastCreateDTO invalidDto = new PodcastCreateDTO();
        invalidDto.setTitle("");

        mockMvc.perform(post("/podcasts/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Проверьте наличие всех обязательных полей и их корректность."));
    }

    @Test
    void createLongTitleValidationTest() throws Exception {
        PodcastCreateDTO invalidDto = new PodcastCreateDTO();
        invalidDto.setTitle("a".repeat(256));

        mockMvc.perform(post("/podcasts/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Проверьте наличие всех обязательных полей и их корректность."));
    }

    @Test
    void updateSuccessTest() throws Exception {
        PodcastUpdateDTO updateDTO = new PodcastUpdateDTO();
        updateDTO.setTitle("New Title");

        PodcastDTO responseDTO = new PodcastDTO();
        given(podcastService.update(eq(1L), any(PodcastUpdateDTO.class))).willReturn(responseDTO);

        mockMvc.perform(put("/podcasts/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(responseDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void updateLongTitleExceptionTest() throws Exception {
        PodcastUpdateDTO invalidDto = new PodcastUpdateDTO();
        invalidDto.setTitle("a".repeat(256));

        mockMvc.perform(put("/podcasts/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteSuccessTest() throws Exception {
        mockMvc.perform(delete("/podcasts/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteNotFoundExceptionTest() throws Exception {
        willThrow(new EntityNotFoundException("Подкаст не найден"))
                .given(podcastService).delete(1L);

        mockMvc.perform(delete("/podcasts/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Подкаст не найден"));
    }

    @Test
    void findEpisodesSuccessTest() throws Exception {
        PodcastEpisodeDTO episode = new PodcastEpisodeDTO();
        given(podcastService.findEpisodesByPodcastId(1L, null, null)).willReturn(List.of(episode));

        mockMvc.perform(get("/podcasts/{podcastId}/episodes", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void findEpisodesWithFiltersTest() throws Exception {
        PodcastEpisodeDTO episode = new PodcastEpisodeDTO();
        given(podcastService.findEpisodesByPodcastId(1L, 1, "Test")).willReturn(List.of(episode));

        mockMvc.perform(get("/podcasts/{podcastId}/episodes", 1L)
                        .param("episodeNumber", "1")
                        .param("title", "Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void addEpisodeSuccessTest() throws Exception {
        PodcastEpisodeCreateDTO createDTO = new PodcastEpisodeCreateDTO();
        createDTO.setEpisodeNumber(1);
        createDTO.setTitle("New Episode");
        createDTO.setAudioUrl("http://example.com/audio.mp3");
        createDTO.setDurationSeconds(1200);

        PodcastEpisodeDTO responseDTO = new PodcastEpisodeDTO();
        given(podcastService.addEpisode(eq(1L), any(PodcastEpisodeCreateDTO.class))).willReturn(responseDTO);

        mockMvc.perform(post("/podcasts/{podcastId}/episodes", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void addEpisodeBlankTitleValidationTest() throws Exception {
        PodcastEpisodeCreateDTO invalidDto = new PodcastEpisodeCreateDTO();
        invalidDto.setEpisodeNumber(1);
        invalidDto.setTitle("");
        invalidDto.setAudioUrl("http://example.com/audio.mp3");
        invalidDto.setDurationSeconds(1200);

        mockMvc.perform(post("/podcasts/{podcastId}/episodes", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Проверьте наличие всех обязательных полей и их корректность."));
    }

    @Test
    void addEpisodeNullEpisodeNumberValidationTest() throws Exception {
        PodcastEpisodeCreateDTO invalidDto = new PodcastEpisodeCreateDTO();
        invalidDto.setEpisodeNumber(null);
        invalidDto.setTitle("Valid Title");
        invalidDto.setAudioUrl("http://example.com/audio.mp3");
        invalidDto.setDurationSeconds(1200);

        mockMvc.perform(post("/podcasts/{podcastId}/episodes", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Проверьте наличие всех обязательных полей и их корректность."));
    }

    @Test
    void addEpisodeNullDurationSecondsValidationTest() throws Exception {
        PodcastEpisodeCreateDTO invalidDto = new PodcastEpisodeCreateDTO();
        invalidDto.setEpisodeNumber(1);
        invalidDto.setTitle("Valid Title");
        invalidDto.setAudioUrl("http://example.com/audio.mp3");
        invalidDto.setDurationSeconds(null);

        mockMvc.perform(post("/podcasts/{podcastId}/episodes", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Проверьте наличие всех обязательных полей и их корректность."));
    }

    @Test
    void addEpisodeLongTitleValidationTest() throws Exception {
        PodcastEpisodeCreateDTO invalidDto = new PodcastEpisodeCreateDTO();
        invalidDto.setEpisodeNumber(1);
        invalidDto.setTitle("a".repeat(256));
        invalidDto.setAudioUrl("http://example.com/audio.mp3");
        invalidDto.setDurationSeconds(1200);

        mockMvc.perform(post("/podcasts/{podcastId}/episodes", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Проверьте наличие всех обязательных полей и их корректность."));
    }

    @Test
    void addEpisodeLongAudioUrlValidationTest() throws Exception {
        PodcastEpisodeCreateDTO invalidDto = new PodcastEpisodeCreateDTO();
        invalidDto.setEpisodeNumber(1);
        invalidDto.setTitle("Valid Title");
        invalidDto.setAudioUrl("a".repeat(1025));
        invalidDto.setDurationSeconds(1200);

        mockMvc.perform(post("/podcasts/{podcastId}/episodes", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Проверьте наличие всех обязательных полей и их корректность."));
    }
}
