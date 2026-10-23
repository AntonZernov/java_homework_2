package ru.content.management.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalTime;

@Data
public class VideoCreateDTO {
    @NotBlank
    @Size(max = 255)
    private String title;
    @NotBlank
    @Size(max = 1024)
    private String url;
    @NotNull
    @Schema(example = "00:04:24")
    private LocalTime duration;
}
