package ru.content.management.controller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.content.management.entity.Article;
import ru.content.management.repository.ArticleRepository;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ArticleRepository articleRepository;

    @BeforeEach
    void setUp() {
        articleRepository.deleteAll();
        Article entity = new Article();
        entity.setTitle("Test title");
        entity.setText("Test text");
        entity.setAuthor("Test author");
        entity.setPublishedAt(LocalDateTime.now());

        articleRepository.save(entity);
    }

    @Test
    void getArticlesSuccessTest() throws Exception {
        mockMvc.perform(get("/articles")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].title").value("Test title"))
                .andExpect(jsonPath("$[0].text").value("Test text"))
                .andExpect(jsonPath("$[0].author").value("Test author"))
                .andExpect(jsonPath("$[0].publishedAt").exists());
    }
}

