package ru.project.board.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.project.board.board.entity.Advertisement;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AdRepo extends JpaRepository<Advertisement, UUID> {

    List<Advertisement> findAll();

    List<Advertisement> getAllByUserId(UUID id);

    List<Advertisement> getAllByCityId(UUID id);

    List<Advertisement> getAllByCategoryId(Long id);

    List<Advertisement> getAllByCityIdAndCategoryId(UUID cityId, Long categoryId);

    Optional<Advertisement> findById(UUID id);
}
