package ru.project.board.board;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.project.board.board.entity.*;
import ru.project.board.board.repository.*;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class BoardApplicationTests {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AdRepo adRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private CityRepo cityRepo;

    @Autowired
    private ImageRepo imageRepo;


    final User user = new User("+79622114167", "1234", "Иван", "Фукин");

    @Test
    @Transactional
    void AddNewUserTest() {
        userRepo.save(user);
        User user1 = userRepo.findByPhoneNumber("+79622114167");
        assertThat(user1.getName()).isEqualTo("Иван");
        assertThat(user1.getSurname()).isEqualTo("Фукин");
    }

    @Test
    @Transactional
    void deleteUserTest() {
        User user1 = userRepo.save(user);
        userRepo.delete(user1);
        assertFalse(userRepo.findById(user1.getId()).isPresent());
    }

    @Test
    @Transactional
    void updateUserInfo() {
        User user1 = userRepo.save(user);
        UUID userId = user1.getId();
        assertThat(userRepo.findById(userId).get().getName()).isEqualTo("Иван");
        user1.setName("Петя");
        userRepo.save(user1);
        assertThat(userRepo.findById(userId).get().getName()).isEqualTo("Петя");
    }

    @Test
    @Transactional
    void addNewAdTest() {
        Advertisement advertisement = new Advertisement();
        User user1 = userRepo.save(user);
        advertisement.setTitle("title");
        advertisement.setUser(user1);
        advertisement = adRepo.save(advertisement);
        assertTrue(adRepo.findById(advertisement.getId()).isPresent());
    }

    @Test
    @Transactional
    void deleteAdTest() {
        Advertisement advertisement = new Advertisement();
        User user1 = userRepo.save(user);
        advertisement.setTitle("title");
        advertisement.setUser(user1);
        advertisement = adRepo.save(advertisement);
        adRepo.delete(advertisement);
        assertFalse(adRepo.findById(advertisement.getId()).isPresent());
    }

    @Test
    @Transactional
    void updateAdInfo() {
        Advertisement ad = new Advertisement();
        User user1 = userRepo.save(user);
        ad.setTitle("title");
        ad.setUser(user1);
        ad = adRepo.save(ad);
        UUID adId = ad.getId();
        assertThat(adRepo.findById(adId).get().getTitle()).isEqualTo("title");
        ad.setTitle("title2");
        adRepo.save(ad);
        assertThat(adRepo.findById(adId).get().getTitle()).isEqualTo("title2");
    }


    @Test
    @Transactional
    void deleteAdAfterDeleteUser() {
        Advertisement advertisement = new Advertisement();
        User user1 = userRepo.save(user);
        advertisement.setTitle("title");
        advertisement.setUser(user);
        advertisement = adRepo.save(advertisement);
        assertTrue(adRepo.findById(advertisement.getId()).isPresent());
        userRepo.deleteById(user1.getId());
        assertFalse(adRepo.findById(advertisement.getId()).isPresent());
    }

    @Test
    @Transactional
    void delete() {
        User user1 = userRepo.save(user);
        Category category = new Category();
        category.setName("category");
        category = categoryRepo.save(category);
        Advertisement ad = new Advertisement();
        ad.setTitle("title");
        ad.setUser(user1);
        ad.setCategory(category);
        ad = adRepo.save(ad);
        categoryRepo.deleteById(category.getId());
        Advertisement ad1 = adRepo.findById(ad.getId()).get();
        System.out.println(ad1.getTitle());
        assertFalse(adRepo.findById(ad.getId()).isPresent());
    }

    @Test
    @Transactional
    void addCity() {
        City city = new City();
        city.setName("title");
        city = cityRepo.save(city);
        assertThat(cityRepo.findById(city.getId()).get().getName()).isEqualTo("title");
    }

    @Test
    @Transactional
    void addImage() {
        Image img = new Image();
        img.setSize((long)5);
        img = imageRepo.save(img);
        assertThat(imageRepo.findById(img.getId()).get().getSize()).isEqualTo(5);
    }





}
