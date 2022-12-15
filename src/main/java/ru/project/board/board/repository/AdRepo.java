package ru.project.board.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.project.board.board.entity.Advertisement;

import java.util.Optional;
import java.util.UUID;

public interface AdRepo extends JpaRepository<Advertisement, UUID> {

    Iterable<Advertisement> getAllByUserId(UUID id);

    Iterable<Advertisement> getAllByCityId(UUID id);

    Iterable<Advertisement> getAllByCategoryId(Long id);

    Iterable<Advertisement> getAllByCityIdAndCategoryId(UUID cityId, Long categoryId);

    Optional<Advertisement> findById(UUID id);
}
