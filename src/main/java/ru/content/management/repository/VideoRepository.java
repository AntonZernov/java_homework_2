package ru.content.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.content.management.entity.Video;

public interface VideoRepository extends JpaRepository<Video, Long>, JpaSpecificationExecutor<Video> {}
