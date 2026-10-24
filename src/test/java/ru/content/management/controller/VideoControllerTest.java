 package ru.content.management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.content.management.dto.VideoCreateDTO;
import ru.content.management.dto.VideoDTO;
import ru.content.management.dto.VideoUpdateDTO;
import ru.content.management.exception.EntityNotFoundException;
import ru.content.management.exception.GlobalExceptionHandler;
import ru.content.management.service.VideoService;

import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.content.management.controller.TestConstants.*;

@WebMvcTest(VideoController.class)
@Import(GlobalExceptionHandler.class)
class VideoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VideoService videoService;

    @Test
    void findSuccessTest() throws Exception {
        VideoDTO video = new VideoDTO();
        given(videoService.find(anyString(), anyString(), any(LocalTime.class), any(LocalTime.class))).willReturn(List.of(video));

        mockMvc.perform(get("/videos/find"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void findByIdSuccessTest() throws Exception {
        VideoDTO video = new VideoDTO();
        given(videoService.findById(1L)).willReturn(video);

        mockMvc.perform(get("/videos/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void findByIdNotFoundTest() throws Exception {
        given(videoService.findById(1L))
                .willThrow(new EntityNotFoundException(VIDEO_NOT_FOUND));

        mockMvc.perform(get("/videos/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value(VIDEO_NOT_FOUND));
    }

    @Test
    void createSuccessTest() throws Exception {
        VideoCreateDTO createDTO = new VideoCreateDTO();
        createDTO.setTitle("Valid Title");
        createDTO.setUrl("https://example.com/video");
        createDTO.setDuration(LocalTime.of(0, 5, 30));

        VideoDTO responseDTO = new VideoDTO();
        given(videoService.create(any(VideoCreateDTO.class))).willReturn(responseDTO);

        mockMvc.perform(post("/videos/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void createBlankTitleValidationTest() throws Exception {
        VideoCreateDTO invalidDto = new VideoCreateDTO();
        invalidDto.setTitle("");
        invalidDto.setUrl("https://example.com/video");
        invalidDto.setDuration(LocalTime.of(0, 5, 30));

        mockMvc.perform(post("/videos/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value(VALIDATION_ERROR_MESSAGE));
    }

    @Test
    void createBlankUrlValidationTest() throws Exception {
        VideoCreateDTO invalidDto = new VideoCreateDTO();
        invalidDto.setTitle("Valid Title");
        invalidDto.setUrl("   ");
        invalidDto.setDuration(LocalTime.of(0, 5, 30));

        mockMvc.perform(post("/videos/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value(VALIDATION_ERROR_MESSAGE));
    }

    @Test
    void createNullDurationValidationTest() throws Exception {
        VideoCreateDTO invalidDto = new VideoCreateDTO();
        invalidDto.setTitle("Valid Title");
        invalidDto.setUrl("https://example.com/video");
        invalidDto.setDuration(null);

        mockMvc.perform(post("/videos/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value(VALIDATION_ERROR_MESSAGE));
    }

    @Test
    void createLongTitleValidationTest() throws Exception {
        VideoCreateDTO invalidDto = new VideoCreateDTO();
        invalidDto.setTitle("a".repeat(256));
        invalidDto.setUrl("https://example.com/video");
        invalidDto.setDuration(LocalTime.of(0, 5, 30));

        mockMvc.perform(post("/videos/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value(VALIDATION_ERROR_MESSAGE));
    }

    @Test
    void createLongUrlValidationTest() throws Exception {
        VideoCreateDTO invalidDto = new VideoCreateDTO();
        invalidDto.setTitle("Valid Title");
        invalidDto.setUrl("a".repeat(1025));
        invalidDto.setDuration(LocalTime.of(0, 5, 30));

        mockMvc.perform(post("/videos/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value(VALIDATION_ERROR_MESSAGE));
    }

    @Test
    void updateSuccessTest() throws Exception {
        VideoUpdateDTO updateDTO = new VideoUpdateDTO();
        updateDTO.setTitle("New Title");

        VideoDTO responseDTO = new VideoDTO();
        given(videoService.update(eq(1L), any(VideoUpdateDTO.class))).willReturn(responseDTO);

        mockMvc.perform(patch("/videos/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void updateLongTitleExceptionTest() throws Exception {
        VideoUpdateDTO invalidDto = new VideoUpdateDTO();
        invalidDto.setTitle("a".repeat(256));

        mockMvc.perform(patch("/videos/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateLongUrlExceptionTest() throws Exception {
        VideoUpdateDTO invalidDto = new VideoUpdateDTO();
        invalidDto.setUrl("a".repeat(1025));

        mockMvc.perform(patch("/videos/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteSuccessTest() throws Exception {
        mockMvc.perform(delete("/videos/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteNotFoundExceptionTest() throws Exception {
        willThrow(new EntityNotFoundException(VIDEO_NOT_FOUND))
                .given(videoService).delete(1L);

        mockMvc.perform(delete("/videos/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value(VIDEO_NOT_FOUND));
    }
}
