package ru.project.board.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.project.board.board.entity.Avatar;

import java.util.Optional;
import java.util.UUID;

public interface AvatarRepo extends JpaRepository<Avatar, UUID> {

    Optional<Avatar> findById(UUID id);
}
