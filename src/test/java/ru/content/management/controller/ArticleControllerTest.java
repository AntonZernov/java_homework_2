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
import ru.content.management.dto.ArticleCreateDTO;
import ru.content.management.dto.ArticleDTO;
import ru.content.management.dto.ArticleUpdateDTO;
import ru.content.management.exception.EntityNotFoundException;
import ru.content.management.exception.GlobalExceptionHandler;
import ru.content.management.service.ArticleService;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.content.management.controller.TestConstants.*;

@WebMvcTest(ArticleController.class)
@Import(GlobalExceptionHandler.class)
class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ArticleService articleService;

    @Test
    void findAllSuccessTest() throws Exception {
        ArticleDTO article = new ArticleDTO();
        given(articleService.find(null, null)).willReturn(List.of(article));

        mockMvc.perform(get("/articles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void findByIdSuccessTest() throws Exception {
        ArticleDTO article = new ArticleDTO();
        given(articleService.findById(1L)).willReturn(article);

        mockMvc.perform(get("/articles/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void findByIdNotFoundTest() throws Exception {
        given(articleService.findById(1L))
                .willThrow(new EntityNotFoundException(ARTICLE_NOT_FOUND));

        mockMvc.perform(get("/articles/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value(ARTICLE_NOT_FOUND));
    }

    @Test
    void createSuccessTest() throws Exception {
        ArticleCreateDTO createDTO = new ArticleCreateDTO();
        createDTO.setTitle("Valid Title");
        createDTO.setText("Valid Text");
        createDTO.setAuthor("Valid Author");

        ArticleDTO responseDTO = new ArticleDTO();
        given(articleService.create(any(ArticleCreateDTO.class))).willReturn(responseDTO);

        mockMvc.perform(post("/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void createBlancFieldsValidationTest() throws Exception {
        ArticleCreateDTO invalidDto = new ArticleCreateDTO();
        invalidDto.setTitle("");
        invalidDto.setText("   ");
        invalidDto.setAuthor("");

        mockMvc.perform(post("/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value(VALIDATION_ERROR_MESSAGE));
    }

    @Test
    void createLongTitleValidationTest() throws Exception {
        ArticleCreateDTO invalidDto = new ArticleCreateDTO();
        invalidDto.setTitle("a".repeat(256));
        invalidDto.setText("Valid Text");
        invalidDto.setAuthor("Valid Author");

        mockMvc.perform(post("/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value(VALIDATION_ERROR_MESSAGE));
    }

    @Test
    void createLongAuthorValidationTest() throws Exception {
        ArticleCreateDTO invalidDto = new ArticleCreateDTO();
        invalidDto.setTitle("Author");
        invalidDto.setText("Valid Text");
        invalidDto.setAuthor("a".repeat(151));

        mockMvc.perform(post("/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value(VALIDATION_ERROR_MESSAGE));
    }

    @Test
    void updateSuccessTest() throws Exception {
        ArticleUpdateDTO updateDTO = new ArticleUpdateDTO();
        updateDTO.setTitle("New Title");

        ArticleDTO responseDTO = new ArticleDTO();
        given(articleService.update(eq(1L), any(ArticleUpdateDTO.class))).willReturn(responseDTO);

        mockMvc.perform(patch("/articles/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk());
    }


    @Test
    void updateLongAuthorExceptionTest() throws Exception {
        ArticleUpdateDTO invalidDto = new ArticleUpdateDTO();
        invalidDto.setAuthor("a".repeat(151));

        mockMvc.perform(patch("/articles/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateLongTitleExceptionTest() throws Exception {
        ArticleUpdateDTO invalidDto = new ArticleUpdateDTO();
        invalidDto.setTitle("a".repeat(256));

        mockMvc.perform(patch("/articles/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteSuccessTest() throws Exception {
        mockMvc.perform(delete("/articles/{id}", 1L))
                .andExpect(status().isNoContent());
    }
    @Test
    void deleteNotFoundExceptionTest() throws Exception {
        willThrow(new EntityNotFoundException(ARTICLE_NOT_FOUND))
                .given(articleService).delete(1L);

        mockMvc.perform(delete("/articles/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value(ARTICLE_NOT_FOUND));
    }
}
