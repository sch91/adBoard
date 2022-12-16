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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.project.board.board.entity.User;
import ru.project.board.board.service.AdService;
import ru.project.board.board.service.CategoryService;
import ru.project.board.board.service.UserService;

import javax.validation.Valid;
import java.io.IOException;

@Controller
public class AppController {

    @Autowired
    private UserService userService;

    @Autowired
    private AdService adService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("listOfCategories", categoryService.getListOfCategories());
        return "login";
    }


    @GetMapping("/registration")
    public String registrationGet(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("listOfCategories", categoryService.getListOfCategories());
        return "registration";
    }

    @PostMapping("/registration")
    public String registrationPost(@Valid @ModelAttribute(value = "user") User user, BindingResult bindingResult, @RequestParam("file") MultipartFile file) throws IOException {
        if (userService.isPhoneNumberAlreadyExists(user.getPhoneNumber())) {
            bindingResult.addError(new FieldError("ad", "phoneNumber", "phone number is already exists"));
        }
        if (bindingResult.hasFieldErrors()) {
            return "registration";
        }
        userService.registration(user, file);

        return "redirect:/login";
    }

    @GetMapping("/my_account")
    public String myAccount(Model model, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        model.addAttribute("listOfAds", adService.getAllByUserId(currentUser.getId()));
        model.addAttribute("user", (User) authentication.getPrincipal());
        model.addAttribute("listOfCategories", categoryService.getListOfCategories());
        return "myAccount";
    }

    @GetMapping("/my_account/edit")
    public String editMyAccountGet(Model model, Authentication authentication) {
        model.addAttribute("user", (User) authentication.getPrincipal());
        return "editProfile";
    }

    @PostMapping("/my_account/edit")
    public String editMyAccountPost(@ModelAttribute(value = "user") User user,
                                    @RequestParam("file") MultipartFile file,
                                    BindingResult bindingResult,
                                    Authentication authentication, Model model
    ) throws IOException {
        User currentUser = (User) authentication.getPrincipal();
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", currentUser);
            return "editProfile";
        }
        userService.edit(currentUser, user, file);
        return "redirect:/my_account";
    }

    @GetMapping("/my_account/delete")
    public String deleteMyAccount(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        userService.deleteUserById(user.getId());
        return "redirect:/logout";
    }

    @GetMapping("/my_ads")
    public String myAds(Model model, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        model.addAttribute("listOfAds", adService.getAllByUserId(currentUser.getId()));
        model.addAttribute("listOfCategories", categoryService.getListOfCategories());
        model.addAttribute("user", currentUser);
        return "adsListByUser";
    }

    @GetMapping("/")
    public String startPage(Model model) {
        model.addAttribute("listOfAds", adService.getAll());
        model.addAttribute("listOfCategories", categoryService.getListOfCategories());
        return "startpage";
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }
}
