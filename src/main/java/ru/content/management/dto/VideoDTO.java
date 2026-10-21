package ru.content.management.dto;

import lombok.Data;
import java.time.LocalTime;

@Data
public class VideoDTO {
    private Long id;
    private String title;
    private String url;
    private LocalTime duration;
}
