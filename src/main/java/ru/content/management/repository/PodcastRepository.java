package ru.content.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.content.management.entity.Podcast;

public interface PodcastRepository extends JpaRepository<Podcast, Long>, JpaSpecificationExecutor<Podcast> {
}
