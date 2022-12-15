package ru.project.board.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.project.board.board.entity.Advertisement;
import ru.project.board.board.entity.Category;
import ru.project.board.board.entity.User;
import ru.project.board.board.exception.AdNotFoundException;
import ru.project.board.board.exception.CategoryNotFoundException;
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
        model.addAttribute("ad", new Advertisement());
        model.addAttribute("listOfCities", cityService.getListOfCities());
        model.addAttribute("listOfCategories", categoryService.getListOfCategories());
        return "newAd";
    }

    @PostMapping("/new")
    public String newAdPost(
            @Valid @ModelAttribute(name = "ad") Advertisement advertisement,
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
        advertisement.setUser((User) authentication.getPrincipal());
        adService.createAd(advertisement, file1, file2, file3);
        return "redirect:/my_ads";
    }

    @GetMapping("/info/{id:^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-4[0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$}")
    public String showAd(@PathVariable("id") UUID id, Model model) {
        try {
            Advertisement advertisement = adService.getAdById(id);
            model.addAttribute("ad", advertisement);
            model.addAttribute("listOfCategories", categoryService.getListOfCategories());
            return "showAd";
        } catch (AdNotFoundException e) {
            return "redirect:/error";
        }
    }

    @GetMapping("/edit/{id:^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-4[0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$}")
    public String editAdGet(@PathVariable("id") UUID id, Model model, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        try {
            Advertisement advertisement = adService.getAdById(id);
            if (!currentUser.getId().equals(advertisement.getUser().getId())) {
                throw new AdNotFoundException("");
            }
            model.addAttribute("ad", advertisement);
            model.addAttribute("listOfCities", cityService.getListOfCities());
            model.addAttribute("listOfCategories", categoryService.getListOfCategories());
        } catch (AdNotFoundException e) {
            return "redirect:/error";
        }
        return "editAd";
    }

    @PostMapping("/edit/{id:^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-4[0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$}")
    public String editAdPost(@PathVariable("id") UUID id,
                             @ModelAttribute(value = "ad") Advertisement advertisement,
                             BindingResult bindingResult) {

        Advertisement currentAd;
        try {
            currentAd = adService.getAdById(id);
        } catch (AdNotFoundException e) {
            return "redirect:/error";
        }
        if (bindingResult.hasErrors()) {
            return "redirect:/ad/edit/" + id;
        }
        adService.edit(currentAd, advertisement);
        return "redirect:/ad/info/" + id;
    }

    @GetMapping("/delete/{id:^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-4[0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$}")
    public String deleteAd(@PathVariable("id") UUID id, Authentication authentication, HttpServletRequest request) {
        String refer = request.getHeader("Referer");
        try {
            Advertisement advertisement = adService.getAdById(id);
            adService.deleteAd(advertisement, authentication);
        } catch (AdNotFoundException e) {
            return "redirect:/error";
        }
        return "redirect:" + refer;
    }

    @GetMapping("/list/by_city/{id:^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-4[0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$}")
    public String listOfAdsByCity(@PathVariable("id") UUID id, Model model) {
        model.addAttribute("listOfAds", adService.getAllByCityId(id));
        return "listOfAdsByCity";
    }

    @GetMapping("/list/by_category/{id:\\d+}")
    public String listOfAdsByCategory(@PathVariable("id") Long id, Model model) {
        try {
            Category category = categoryService.getCategoryById(id);
            model.addAttribute("category", category);
        } catch (CategoryNotFoundException e) {
            return "redirect:/error";
        }
        model.addAttribute("listOfAds", adService.getAllByCategoryId(id));
        model.addAttribute("listOfCategories", categoryService.getListOfCategories());
        return "adsByCategory";
    }

    @GetMapping("/list/by_city/{cityId:^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-4[0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$}/by_category/{categoryId:\\d+}")
    public String listOfAdsByCityAndCategory(@PathVariable("cityId") UUID cityId, @PathVariable("categoryId") Long categoryId, Model model) {
        model.addAttribute("listOfAds", adService.getAllByCityIdAndCategoryId(cityId, categoryId));
        return "listOfAdsByCityAndCategory";

    }
}
