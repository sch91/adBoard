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

    @GetMapping("/{id}/ads")
    public String adsByUser(@PathVariable("id") UUID id, Model model) {
        try {
            User user = userService.getUserById(id);
            model.addAttribute("user", user);
            Iterable<Ad> listOfAds = adService.getAllByUserId(user.getId());
            model.addAttribute("listOfAds", listOfAds);
            return "adsListByUser";

        } catch (UserNotFoundException e) {
            return "error";
        }
    }
}
