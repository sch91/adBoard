package ru.project.board.board.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.project.board.board.entity.Ad;
import ru.project.board.board.exception.AdNotFoundException;
import ru.project.board.board.repository.AdRepo;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AdService {
    @Autowired
    private AdRepo adRepo;

    public Ad getAdById(UUID id) throws AdNotFoundException {
        Ad ad = adRepo.findById(id).get();
        if (ad == null) {
            throw new NullPointerException("ad not found");
        }
        return ad;
    }

    public Iterable<Ad> getAllByUserId(UUID id) {
        return adRepo.getAllByUserId(id);
    }

    public Iterable<Ad> getAll() {
        return adRepo.findAll();
    }

    public void createAd(Ad ad) {
        ad.setDateOfCreation(LocalDateTime.now());
        adRepo.save(ad);
    }
}
