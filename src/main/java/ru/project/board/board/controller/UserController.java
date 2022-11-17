package ru.project.board.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.project.board.board.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {


    @Autowired
    private UserService userService;

}
