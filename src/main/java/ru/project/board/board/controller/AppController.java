package ru.project.board.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.project.board.board.entity.User;
import ru.project.board.board.service.AdService;
import ru.project.board.board.service.UserService;

import javax.validation.Valid;

@Controller
public class AppController {

    @Autowired
    private UserService userService;

    @Autowired
    private AdService adService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registration")
    public String registrationGet(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String registrationPost(@Valid @ModelAttribute(value = "user") User user, BindingResult bindingResult) {
        if (userService.isPhoneNumberAlreadyExists(user.getPhoneNumber())) {
            bindingResult.addError(new FieldError("ad","phoneNumber", "phone number is already exists"));
        }
        if (bindingResult.hasFieldErrors()) {
            return "registration";
        }
        userService.registration(user);
        return "redirect:/";
    }

    @GetMapping("/my_account")
    public String myAccount(Model model, Authentication authentication) {
        model.addAttribute("user", (User) authentication.getPrincipal());
        return "myAccount";
    }

    @GetMapping("/my_ads")
    public String myAds(Model model, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        model.addAttribute("listOfAds", adService.getAllByUserId(currentUser.getId()));
        model.addAttribute("user", currentUser);
        return "adsListByUser";
    }

    @GetMapping("/")
    public String startPage() {
        return "startpage";
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }
}
