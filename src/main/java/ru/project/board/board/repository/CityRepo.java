package ru.project.board.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.project.board.board.entity.City;

import java.util.Optional;
import java.util.UUID;

public interface CityRepo extends JpaRepository<City, UUID> {
    Optional<City> findById(UUID id);
}
