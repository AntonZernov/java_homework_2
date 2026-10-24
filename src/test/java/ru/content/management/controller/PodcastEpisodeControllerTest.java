package ru.content.management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.content.management.dto.PodcastEpisodeDTO;
import ru.content.management.dto.PodcastEpisodeUpdateDTO;
import ru.content.management.exception.EntityNotFoundException;
import ru.content.management.exception.GlobalExceptionHandler;
import ru.content.management.service.PodcastService;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.content.management.controller.TestConstants.*;

@WebMvcTest(PodcastEpisodeController.class)
@Import(GlobalExceptionHandler.class)
class PodcastEpisodeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PodcastService podcastService;

    @Test
    void findByIdSuccessTest() throws Exception {
        PodcastEpisodeDTO episode = new PodcastEpisodeDTO();
        given(podcastService.findEpisodeById(1L)).willReturn(episode);

        mockMvc.perform(get("/episodes/{episodeId}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void findByIdNotFoundTest() throws Exception {
        given(podcastService.findEpisodeById(1L))
                .willThrow(new EntityNotFoundException(EPISODE_NOT_FOUND));

        mockMvc.perform(get("/episodes/{episodeId}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value(EPISODE_NOT_FOUND));
    }

    @Test
    void updateSuccessTest() throws Exception {
        PodcastEpisodeUpdateDTO updateDTO = new PodcastEpisodeUpdateDTO();
        updateDTO.setTitle("Updated Episode");

        PodcastEpisodeDTO responseDTO = new PodcastEpisodeDTO();
        given(podcastService.updateEpisode(eq(1L), any(PodcastEpisodeUpdateDTO.class))).willReturn(responseDTO);

        mockMvc.perform(patch("/episodes/{episodeId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void updateLongTitleValidationTest() throws Exception {
        PodcastEpisodeUpdateDTO invalidDto = new PodcastEpisodeUpdateDTO();
        invalidDto.setTitle("a".repeat(256));

        mockMvc.perform(patch("/episodes/{episodeId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateLongAudioUrlValidationTest() throws Exception {
        PodcastEpisodeUpdateDTO invalidDto = new PodcastEpisodeUpdateDTO();
        invalidDto.setAudioUrl("a".repeat(1025));

        mockMvc.perform(patch("/episodes/{episodeId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteSuccessTest() throws Exception {
        mockMvc.perform(delete("/episodes/{episodeId}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteNotFoundTest() throws Exception {
        willThrow(new EntityNotFoundException(EPISODE_NOT_FOUND))
                .given(podcastService).deleteEpisode(1L);

        mockMvc.perform(delete("/episodes/{episodeId}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value(EPISODE_NOT_FOUND));
    }
}
