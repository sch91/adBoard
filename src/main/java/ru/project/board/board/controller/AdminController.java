package ru.project.board.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.project.board.board.entity.Advertisement;
import ru.project.board.board.entity.Role;
import ru.project.board.board.entity.User;
import ru.project.board.board.exception.AdNotFoundException;
import ru.project.board.board.service.AdService;
import ru.project.board.board.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    @Autowired
    private AdService adService;

    @GetMapping("/ads")
    public String listOfAds(Model model, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        if (user.getRole().equals(Role.ROLE_ADMIN)) {
            model.addAttribute("listOfAds", adService.getAll());
            return "adminListOfAds";
        } else {
            return "redirect:../error";
        }
    }

    @GetMapping("/users")
    public String userList(Model model, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        if (user.getRole().equals(Role.ROLE_ADMIN)) {
            model.addAttribute("listOfUsers", userService.getAllUsers());
            return "adminListOfUsers";
        } else {
            return "redirect:../error";
        }
    }

    @GetMapping("/delete/user/{id:^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-4[0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$}")
    public String deleteUser(@PathVariable("id") UUID id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        if (user.getRole().equals(Role.ROLE_ADMIN)) {
            userService.deleteUserById(id);
            return "redirect:/admin/users";
        } else {
            return "redirect:../error";
        }
    }

    @GetMapping("/delete/ad/{id:^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-4[0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$}")
    public String deleteAd(@PathVariable("id") UUID id, Authentication authentication) {
        try {
            Advertisement advertisement = adService.getAdById(id);
            adService.deleteAd(advertisement, authentication);
        } catch (AdNotFoundException e) {
            return "redirect:/error";
        }
        return "redirect:/admin/ads";
    }
}
