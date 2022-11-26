package ru.project.board.board.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.project.board.board.entity.Ad;
import ru.project.board.board.entity.Image;
import ru.project.board.board.entity.Role;
import ru.project.board.board.entity.User;
import ru.project.board.board.exception.AdNotFoundException;
import ru.project.board.board.repository.AdRepo;
import ru.project.board.board.repository.ImageRepo;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AdService {
    @Autowired
    private AdRepo adRepo;

    @Autowired
    private ImageRepo imageRepo;

    public Ad getAdById(UUID id) throws AdNotFoundException {
        return adRepo.findById(id).orElseThrow(() -> new AdNotFoundException("Ad not found"));
    }

    public Iterable<Ad> getAllByUserId(UUID id) {
        return adRepo.getAllByUserId(id);
    }

    public Iterable<Ad> getAll() {
        return adRepo.findAll();
    }

    public void createAd(Ad ad, MultipartFile file1, MultipartFile file2, MultipartFile file3) throws IOException {
        List<Image> images = new ArrayList<>();
        ad = adRepo.save(ad);
        if (file1.getSize() != 0) {
            Image img = toImage(file1, ad);
            img = imageRepo.save(img);
            images.add(img);
        }
        if (file2.getSize() != 0) {
            Image img = toImage(file2, ad);
            img = imageRepo.save(img);
            images.add(img);
        }
        if (file3.getSize() != 0) {
            Image img = toImage(file3, ad);
            img = imageRepo.save(img);
            images.add(img);
        }
        ad.setDateOfCreation(LocalDateTime.now());
        ad.setImageList(images);
        adRepo.save(ad);
    }

    private Image toImage(MultipartFile file, Ad ad) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        image.setAd(ad);
        return image;
    }

    public void deleteAd(Ad ad, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        if (currentUser.getId().equals(ad.getUser().getId()) || currentUser.getRole().equals(Role.ROLE_ADMIN)) {
            adRepo.deleteById(ad.getId());
        }

    }
}
