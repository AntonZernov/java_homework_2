package ru.content.management.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalTime;

@Data
public class VideoUpdateDTO {
    @Size(max = 255)
    private String title;
    @Size(max = 1024)
    private String url;
    private LocalTime duration;
}
