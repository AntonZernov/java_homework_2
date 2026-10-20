package ru.content.management.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalTime;

@Data
public class VideoUpdateDTO {
    @Size(max = 255)
    private String title;
    @Size(max = 1024)
    private String url;
    @Schema(example = "00:04:24")
    private LocalTime duration;
}
