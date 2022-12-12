package ru.project.board.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.project.board.board.entity.Ad;
import ru.project.board.board.entity.User;
import ru.project.board.board.exception.AdNotFoundException;
import ru.project.board.board.service.AdService;
import ru.project.board.board.service.CategoryService;
import ru.project.board.board.service.CityService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/ad")
public class AdController {
    @Autowired
    private AdService adService;

    @Autowired
    private CityService cityService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/new")
    public String newAdGet(Model model, Authentication authentication) {
        model.addAttribute("user", (User) authentication.getPrincipal());
        model.addAttribute("ad", new Ad());
        model.addAttribute("listOfCities", cityService.getListOfCities());
        model.addAttribute("listOfCategories", categoryService.getListOfCategories());
        return "newAd";
    }

    @PostMapping("/new")
    public String newAdPost(
            @Valid @ModelAttribute(name = "ad") Ad ad,
            BindingResult bindingResult,
            Authentication authentication,
            Model model,
            @RequestParam("file1") MultipartFile file1,
            @RequestParam("file2") MultipartFile file2,
            @RequestParam("file3") MultipartFile file3
    ) throws IOException {
        if (bindingResult.hasFieldErrors()) {
            model.addAttribute("user", (User) authentication.getPrincipal());
            model.addAttribute("listOfCities", cityService.getListOfCities());
            model.addAttribute("listOfCategories", categoryService.getListOfCategories());
            return "newAd";
        }
        ad.setUser((User) authentication.getPrincipal());
        adService.createAd(ad, file1, file2, file3);
        return "redirect:/my_ads";
    }

    @GetMapping("/info/{id:^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-4[0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$}")
    public String showAd(@PathVariable("id") UUID id, Model model) {
        try {
            Ad ad = adService.getAdById(id);
            model.addAttribute("ad", ad);
            return "showAd";
        } catch (AdNotFoundException e) {
            return "redirect:/error";
        }
    }

    @GetMapping("/delete/{id:^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-4[0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$}")
    public String deleteAd(@PathVariable("id") UUID id, Authentication authentication, HttpServletRequest request) {
        String refer = request.getHeader("Referer");
        try {
            Ad ad = adService.getAdById(id);
            adService.deleteAd(ad, authentication);
        } catch (AdNotFoundException e) {
            return "redirect:/error";
        }
        return "redirect:" + refer;
    }

    @GetMapping("list")
    public String listOfAds(Model model) {
        model.addAttribute("listOfAds", adService.getAll());
        return "listOfAds";
    }

    @GetMapping("/list/by_city/{id:^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-4[0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$}")
    public String listOfAdsByCity(@PathVariable("id") UUID id, Model model) {
        model.addAttribute("listOfAds", adService.getAllByCityId(id));
        return "listOfAdsByCity";
    }

    @GetMapping("/list/by_category/{id:\\d+}")
    public String listOfAdsByCategory(@PathVariable("id") Long id, Model model) {
        model.addAttribute("listOfAds", adService.getAllByCategoryId(id));
        return "listOfAdsByCategory";
    }

    @GetMapping("/list/by_city/{cityId:^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-4[0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$}/by_category/{categoryId:\\d+}")
    public String listOfAdsByCityAndCategory(@PathVariable("cityId") UUID cityId, @PathVariable("categoryId") Long categoryId, Model model) {
        model.addAttribute("listOfAds", adService.getAllByCityIdAndCategoryId(cityId, categoryId));
        return "listOfAdsByCityAndCategory";

    }
}
