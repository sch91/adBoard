package ru.project.board.board.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.project.board.board.entity.Avatar;
import ru.project.board.board.repository.AvatarRepo;

import java.util.UUID;

@Service
public class AvatarService {

    @Autowired
    private AvatarRepo avatarRepo;

    public Avatar findById(UUID id) {
        return avatarRepo.findById(id).orElse(null);
    }
}
