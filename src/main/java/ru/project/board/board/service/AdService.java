package ru.project.board.board.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

    public List<Advertisement> getAllByUserId(UUID id) {
        return adRepo.getAllByUserId(id, Sort.by(Sort.Order.desc("dateOfCreation")));
    }

    public List<Advertisement> getAllByCityId(UUID id) {
        return adRepo.getAllByCityId(id, Sort.by(Sort.Order.desc("dateOfCreation")));
    }

    public List<Advertisement> getAllByCategoryId(Long id) {
        return adRepo.getAllByCategoryId(id, Sort.by(Sort.Order.desc("dateOfCreation")));
    }

    public List<Advertisement> getAllByCityIdAndCategoryId(UUID cityId, Long categoryId) {
        return adRepo.getAllByCityIdAndCategoryId(cityId, categoryId, Sort.by(Sort.Order.desc("dateOfCreation")));
    }

    public List<Advertisement> getAll() {
        return adRepo.findAll(Sort.by(Sort.Order.desc("dateOfCreation")));
    }

    public void createAd(Advertisement advertisement, MultipartFile file1, MultipartFile file2, MultipartFile file3) throws IOException {
        advertisement = adRepo.save(advertisement);
        advertisement.setDateOfCreation(LocalDateTime.now());
        advertisement.setImageList(generateListOfImages(file1, file2, file3, advertisement));
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
        image = imageRepo.save(image);
        return image;
    }

    private ArrayList<Image> generateListOfImages(MultipartFile file1, MultipartFile file2, MultipartFile file3, Advertisement ad) throws IOException {
        ArrayList<Image> images = new ArrayList<>();
        if (file1.getSize() != 0) {
            images.add(toImage(file1, ad));
        }
        if (file2.getSize() != 0) {
            images.add(toImage(file2, ad));
        }
        if (file3.getSize() != 0) {
            images.add(toImage(file3, ad));
        }
        return images;
    }

    public void edit(Advertisement currentAd, Advertisement advertisement) {
        currentAd.setCategory(advertisement.getCategory());
        currentAd.setCity(advertisement.getCity());
        currentAd.setDescription(advertisement.getDescription());
        currentAd.setPrice(advertisement.getPrice());
        currentAd.setTitle(advertisement.getTitle());
        adRepo.save(currentAd);
    }

    public void deleteAd(Advertisement advertisement, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        if (currentUser.getId().equals(advertisement.getUser().getId()) || currentUser.getRole().equals(Role.ROLE_ADMIN)) {
            adRepo.deleteById(advertisement.getId());
        }

    }
}
