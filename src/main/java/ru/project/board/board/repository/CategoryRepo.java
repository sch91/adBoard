package ru.project.board.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.project.board.board.entity.Category;

import java.util.Optional;

public interface CategoryRepo extends JpaRepository<Category, Long> {
    Optional<Category> findById(Long id);
}
