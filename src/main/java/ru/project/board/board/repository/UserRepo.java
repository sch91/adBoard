package ru.project.board.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.project.board.board.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepo extends JpaRepository<User, UUID> {

    User findByPhoneNumber(String phoneNumber);

    Optional<User> findById(UUID id);
}
