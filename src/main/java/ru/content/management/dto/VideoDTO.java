package ru.content.management.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalTime;

@Data
public class VideoDTO {
    private Long id;
    private String title;
    private String url;
    @Schema(example = "00:04:24")
    private LocalTime duration;
}
