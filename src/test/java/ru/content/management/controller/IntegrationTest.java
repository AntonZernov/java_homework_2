package ru.content.management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.content.management.dto.PodcastEpisodeUpdateDTO;
import ru.content.management.dto.PodcastUpdateDTO;
import ru.content.management.entity.Article;
import ru.content.management.entity.Podcast;
import ru.content.management.entity.PodcastEpisode;
import ru.content.management.entity.Video;
import ru.content.management.repository.ArticleRepository;
import ru.content.management.repository.PodcastEpisodeRepository;
import ru.content.management.repository.PodcastRepository;
import ru.content.management.repository.VideoRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private PodcastRepository podcastRepository;

    @Autowired
    private PodcastEpisodeRepository podcastEpisodeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        articleRepository.deleteAll();
        videoRepository.deleteAll();
        podcastEpisodeRepository.deleteAll();
        podcastRepository.deleteAll();
    }

    @Test
    void getArticlesSuccessTest() throws Exception {
        Article entity = new Article();
        entity.setTitle("Test title");
        entity.setText("Test text");
        entity.setAuthor("Test author");
        entity.setPublishedAt(LocalDateTime.now());
        articleRepository.save(entity);

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

    @Test
    void getVideoByIdSuccessTest() throws Exception {
        Video video = new Video();
        video.setTitle("Test Video");
        video.setUrl("https://example.com/test");
        video.setDuration(LocalTime.of(0, 10, 30));
        videoRepository.save(video);
        mockMvc.perform(get("/videos/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Video"))
                .andExpect(jsonPath("$.url").value("https://example.com/test"))
                .andExpect(jsonPath("$.duration").exists());
    }

    @Test
    void createPodcastSuccessTest() throws Exception {
        PodcastUpdateDTO createDTO = new PodcastUpdateDTO();
        createDTO.setTitle("New Podcast");

        mockMvc.perform(post("/podcasts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("New Podcast"));
    }

    @Test
    void updateEpisodeSuccessTest() throws Exception {
        Podcast podcast = new Podcast();
        podcast.setTitle("Test Podcast");
        podcast = podcastRepository.save(podcast);

        PodcastEpisode episode = new PodcastEpisode();
        episode.setPodcastId(podcast.getId());
        episode.setEpisodeNumber(1);
        episode.setTitle("Original Episode Title");
        episode.setAudioUrl("https://example.com/episode.mp3");
        episode.setDuration(LocalTime.of(0, 15, 0));
        podcastEpisodeRepository.save(episode);

        PodcastEpisodeUpdateDTO updateDTO = new PodcastEpisodeUpdateDTO();
        updateDTO.setTitle("Updated Episode Title");
        updateDTO.setAudioUrl("https://example.com/updated.mp3");

        mockMvc.perform(patch("/episodes/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Updated Episode Title"))
                .andExpect(jsonPath("$.audioUrl").value("https://example.com/updated.mp3"));
    }
}

