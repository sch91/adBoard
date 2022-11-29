package ru.project.board.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.project.board.board.entity.Role;
import ru.project.board.board.entity.User;
import ru.project.board.board.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String userList(Model model, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        if (user.getRole().equals(Role.ROLE_ADMIN)) {
            model.addAttribute("userList", userService.getAllUsers());
            return "listOfUsers";
        } else {
            return "redirect::/error";
        }
    }
}
