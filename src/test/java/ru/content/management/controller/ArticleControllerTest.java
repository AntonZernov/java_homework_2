package ru.content.management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.content.management.App;
import ru.content.management.BaseMvcTest;
import ru.content.management.dto.ArticleCreateDTO;
import ru.content.management.dto.ArticleDTO;
import ru.content.management.dto.ArticleUpdateDTO;
import ru.content.management.exception.EntityNotFoundException;
import ru.content.management.exception.GlobalExceptionHandler;
import ru.content.management.service.ArticleService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ArticleController.class)
@ContextConfiguration(classes = {App.class, GlobalExceptionHandler.class})
class ArticleControllerTest extends BaseMvcTest {

    @MockBean
    private ArticleService articleService;

    @Test
    void findAll_ShouldReturnListAnd200() throws Exception {
        ArticleDTO article = new ArticleDTO();
        given(articleService.findAll()).willReturn(List.of(article));

        mockMvc.perform(get("/article/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void findById_ShouldReturnArticleAnd200() throws Exception {
        ArticleDTO article = new ArticleDTO();
        given(articleService.findById(1L)).willReturn(article);

        mockMvc.perform(get("/article/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void create_ShouldReturnCreatedArticleAnd21() throws Exception {
        ArticleCreateDTO createDTO = new ArticleCreateDTO();
        createDTO.setTitle("Valid Title");
        createDTO.setText("Valid Text");
        createDTO.setAuthor("Valid Author");

        ArticleDTO responseDTO = new ArticleDTO();

        given(articleService.create(any(ArticleCreateDTO.class))).willReturn(responseDTO);

        mockMvc.perform(post("/article/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void delete_ShouldReturn204() throws Exception {
        mockMvc.perform(delete("/article/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    // --- ТЕСТЫ: ОШИБКИ ВАЛИДАЦИИ (400 BAD REQUEST) ---

    @Test
    void create_ShouldReturn400WhenFieldsAreBlank() throws Exception {
        ArticleCreateDTO invalidDto = new ArticleCreateDTO();
        invalidDto.setTitle(""); // Нарушение @NotBlank
        invalidDto.setText("   "); // Нарушение @NotBlank
        invalidDto.setAuthor(""); // Нарушение @NotBlank

        mockMvc.perform(post("/article/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Проверьте наличие всех обязательных полей и их корректность."));
    }

    @Test
    void create_ShouldReturn400WhenTitleIsTooLong() throws Exception {
        ArticleCreateDTO invalidDto = new ArticleCreateDTO();
        invalidDto.setTitle("a".repeat(256)); // Нарушение @Size(max = 255)
        invalidDto.setText("Valid Text");
        invalidDto.setAuthor("Valid Author");

        mockMvc.perform(post("/article/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_ShouldReturn400WhenAuthorIsTooLong() throws Exception {
        ArticleUpdateDTO invalidDto = new ArticleUpdateDTO();
        invalidDto.setAuthor("a".repeat(151)); // Нарушение @Size(max = 150)

        mockMvc.perform(put("/article/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    // --- ТЕСТЫ: ОБРАБОТКА ИСКЛЮЧЕНИЙ СЕРВИСА (404 NOT FOUND) ---

    @Test
    void findById_ShouldReturn404WhenNotFound() throws Exception {
        given(articleService.findById(1L))
                .willThrow(new EntityNotFoundException("Статья с ID 1 не найдена"));

        mockMvc.perform(get("/article/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Статья с ID 1 не найдена"));
    }

    @Test
    void delete_ShouldReturn404WhenNotFound() throws Exception {
        // Для void-методов синтаксис Mockito отличается
        willThrow(new EntityNotFoundException("Статья не найдена"))
                .given(articleService).delete(1L);

        mockMvc.perform(delete("/article/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}
