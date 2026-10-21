package ru.content.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.content.management.entity.Video;

public interface VideoRepository extends JpaRepository<Video, Long> {}
