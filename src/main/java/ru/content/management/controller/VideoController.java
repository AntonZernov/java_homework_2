package ru.content.management.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.content.management.dto.VideoCreateDTO;
import ru.content.management.dto.VideoDTO;
import ru.content.management.dto.VideoUpdateDTO;
import ru.content.management.service.VideoService;
import java.util.List;

@RestController
@RequestMapping("/video")
@Tag(name = "Video", description = "API для управления видео")
public class VideoController {

    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @GetMapping("/find")
    @Operation(summary = "Поиск видео")
    @ResponseStatus(HttpStatus.OK)
    public List<VideoDTO> find(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String url,
            @RequestParam(required = false) String durationFrom,
            @RequestParam(required = false) String durationTo) {
        return videoService.find(title, url, durationFrom, durationTo);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить видео по ID")
    @ResponseStatus(HttpStatus.OK)
    public VideoDTO findById(@PathVariable Long id) {
        return videoService.findById(id);
    }

    @PostMapping("/create")
    @Operation(summary = "Создать видео")
    @ResponseStatus(HttpStatus.CREATED)
    public VideoDTO create(@Valid @RequestBody VideoCreateDTO dto) {
        return videoService.create(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить видео")
    @ResponseStatus(HttpStatus.OK)
    public VideoDTO update(@PathVariable Long id, @Valid @RequestBody VideoUpdateDTO dto) {
        return videoService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить видео")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        videoService.delete(id);
    }
}
