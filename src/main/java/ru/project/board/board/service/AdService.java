package ru.project.board.board.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.project.board.board.entity.Advertisement;
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

    public Advertisement getAdById(UUID id) throws AdNotFoundException {
        return adRepo.findById(id).orElseThrow(() -> new AdNotFoundException("Ad not found"));
    }

    public Iterable<Advertisement> getAllByUserId(UUID id) {
        return adRepo.getAllByUserId(id);
    }

    public Iterable<Advertisement> getAllByCityId(UUID id) {
        return adRepo.getAllByCityId(id);
    }

    public Iterable<Advertisement> getAllByCategoryId(Long id) {
        return adRepo.getAllByCategoryId(id);
    }

    public Iterable<Advertisement> getAllByCityIdAndCategoryId(UUID cityId, Long categoryId) {
        return adRepo.getAllByCityIdAndCategoryId(cityId, categoryId);
    }

    public Iterable<Advertisement> getAll() {
        return adRepo.findAll();
    }

    public void createAd(Advertisement advertisement, MultipartFile file1, MultipartFile file2, MultipartFile file3) throws IOException {
        List<Image> images = new ArrayList<>();
        advertisement = adRepo.save(advertisement);
        if (file1.getSize() != 0) {
            Image img = toImage(file1, advertisement);
            img = imageRepo.save(img);
            images.add(img);
        }
        if (file2.getSize() != 0) {
            Image img = toImage(file2, advertisement);
            img = imageRepo.save(img);
            images.add(img);
        }
        if (file3.getSize() != 0) {
            Image img = toImage(file3, advertisement);
            img = imageRepo.save(img);
            images.add(img);
        }
        advertisement.setDateOfCreation(LocalDateTime.now());
        advertisement.setImageList(images);
        adRepo.save(advertisement);
    }

    private Image toImage(MultipartFile file, Advertisement advertisement) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        image.setAdvertisement(advertisement);
        return image;
    }

    public void deleteAd(Advertisement advertisement, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        if (currentUser.getId().equals(advertisement.getUser().getId()) || currentUser.getRole().equals(Role.ROLE_ADMIN)) {
            adRepo.deleteById(advertisement.getId());
        }

    }
}
