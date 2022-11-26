package ru.project.board.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.project.board.board.entity.Ad;
import ru.project.board.board.entity.User;
import ru.project.board.board.exception.UserNotFoundException;
import ru.project.board.board.service.AdService;
import ru.project.board.board.service.UserService;

import java.util.UUID;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AdService adService;

    @GetMapping("/{id:^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-4[0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$}/ads")
    public String adsByUser(@PathVariable("id") UUID id, Model model) {
        try {
            User user = userService.getUserById(id);
            model.addAttribute("user", user);
            Iterable<Ad> listOfAds = adService.getAllByUserId(user.getId());
            model.addAttribute("listOfAds", listOfAds);
            return "adsListByUser";

        } catch (UserNotFoundException e) {
            model.addAttribute("errorMessage", "User not found");
            return "error";
        }
    }
}
