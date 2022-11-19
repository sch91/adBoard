package ru.project.board.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.project.board.board.entity.Ad;
import ru.project.board.board.entity.User;
import ru.project.board.board.exception.AdNotFoundException;
import ru.project.board.board.service.AdService;
import ru.project.board.board.service.CategoryService;
import ru.project.board.board.service.CityService;

import javax.validation.Valid;
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
    public String newAdPost(@Valid @ModelAttribute(name = "ad") Ad ad, BindingResult bindingResult, Authentication authentication, Model model) {
        if (bindingResult.hasFieldErrors()) {
            model.addAttribute("user", (User) authentication.getPrincipal());
            model.addAttribute("listOfCities", cityService.getListOfCities());
            model.addAttribute("listOfCategories", categoryService.getListOfCategories());
            return "newAd";
        }
        ad.setUser((User) authentication.getPrincipal());
        adService.createAd(ad);
        return "redirect:/my_ads";
    }

    @GetMapping("/{id}")
    public String showAd(@PathVariable("id") UUID id, Model model) {
        try {
            Ad ad = adService.getAdById(id);
            model.addAttribute("ad", ad);
            return "showAd";
        } catch (AdNotFoundException e) {
            return "error";
        }
    }
}
