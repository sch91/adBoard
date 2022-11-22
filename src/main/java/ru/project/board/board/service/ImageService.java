package ru.project.board.board.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.project.board.board.entity.Image;
import ru.project.board.board.repository.ImageRepo;

import java.util.UUID;

@Service
public class ImageService {

    @Autowired
    private ImageRepo imageRepo;

    public Image findById(UUID id) {
        return imageRepo.findById(id).orElse(null);
    }
}
