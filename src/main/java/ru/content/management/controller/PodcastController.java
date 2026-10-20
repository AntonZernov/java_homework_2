package ru.content.management.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.content.management.dto.PodcastCreateDTO;
import ru.content.management.dto.PodcastDTO;
import ru.content.management.dto.PodcastEpisodeCreateDTO;
import ru.content.management.dto.PodcastEpisodeDTO;
import ru.content.management.dto.PodcastUpdateDTO;
import ru.content.management.service.PodcastService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/podcasts")
@Tag(name = "Podcast", description = "API для управления подкастами")
public class PodcastController {

    private final PodcastService podcastService;

    @GetMapping
    @Operation(summary = "Поиск подкастов")
    @ResponseStatus(HttpStatus.OK)
    public List<PodcastDTO> find(
            @RequestParam(required = false) @Size(max = 255) String title) {
        return podcastService.find(title);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить подкаст по ID")
    @ResponseStatus(HttpStatus.OK)
    public PodcastDTO findById(@PathVariable Long id) {
        return podcastService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Создать подкаст")
    @ResponseStatus(HttpStatus.CREATED)
    public PodcastDTO create(@Valid @RequestBody PodcastCreateDTO dto) {
        return podcastService.create(dto);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновить подкаст")
    @ResponseStatus(HttpStatus.OK)
    public PodcastDTO update(@PathVariable Long id, @Valid @RequestBody PodcastUpdateDTO dto) {
        return podcastService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить подкаст")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        podcastService.delete(id);
    }

    @GetMapping("/{podcastId}/episodes")
    @Operation(summary = "Получить эпизоды подкаста с фильтрацией")
    @ResponseStatus(HttpStatus.OK)
    public List<PodcastEpisodeDTO> findEpisodes(
            @PathVariable Long podcastId,
            @RequestParam(required = false) Integer episodeNumber,
            @RequestParam(required = false) @Size(max = 255) String title) {
        return podcastService.findEpisodesByPodcastId(podcastId, episodeNumber, title);
    }

    @PostMapping("/{podcastId}/episodes")
    @Operation(summary = "Добавить новый эпизод в подкаст")
    @ResponseStatus(HttpStatus.CREATED)
    public PodcastEpisodeDTO addEpisode(
            @PathVariable Long podcastId,
            @Valid @RequestBody PodcastEpisodeCreateDTO dto) {
        return podcastService.addEpisode(podcastId, dto);
    }
}
