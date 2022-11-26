package ru.project.board.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.project.board.board.entity.Ad;

import java.util.Optional;
import java.util.UUID;

public interface AdRepo extends JpaRepository<Ad, UUID> {

    Iterable<Ad> getAllByUserId(UUID id);

    Iterable<Ad> getAllByCityId(UUID id);

    Iterable<Ad> getAllByCategoryId(Long id);

    Iterable<Ad> getAllByCityIdAndCategoryId(UUID cityId, Long categoryId);

    Optional<Ad> findById(UUID id);
}
