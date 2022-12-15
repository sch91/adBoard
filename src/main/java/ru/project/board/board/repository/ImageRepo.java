package ru.project.board.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.project.board.board.entity.Image;

import java.util.Optional;
import java.util.UUID;

public interface ImageRepo extends JpaRepository<Image, UUID> {
    Optional<Image> findById(UUID id);
}
