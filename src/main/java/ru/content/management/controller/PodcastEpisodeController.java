package ru.content.management.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.content.management.dto.PodcastEpisodeDTO;
import ru.content.management.dto.PodcastEpisodeUpdateDTO;
import ru.content.management.service.PodcastService;

@RestController
@RequestMapping("/episodes")
@Tag(name = "PodcastEpisode", description = "API для управления эпизодами подкастов")
public class PodcastEpisodeController {

    private final PodcastService podcastService;

    public PodcastEpisodeController(PodcastService podcastService) {
        this.podcastService = podcastService;
    }

    @GetMapping("/{episodeId}")
    @Operation(summary = "Получить эпизод по ID")
    @ResponseStatus(HttpStatus.OK)
    public PodcastEpisodeDTO findById(@PathVariable Long episodeId) {
        return podcastService.findEpisodeById(episodeId);
    }

    @PatchMapping("/{episodeId}")
    @Operation(summary = "Обновить эпизод")
    @ResponseStatus(HttpStatus.OK)
    public PodcastEpisodeDTO update(@PathVariable Long episodeId, @Valid @RequestBody PodcastEpisodeUpdateDTO dto) {
        return podcastService.updateEpisode(episodeId, dto);
    }

    @DeleteMapping("/{episodeId}")
    @Operation(summary = "Удалить эпизод")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long episodeId) {
        podcastService.deleteEpisode(episodeId);
    }
}
