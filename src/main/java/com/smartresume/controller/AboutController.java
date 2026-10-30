package com.smartresume.controller;

import com.smartresume.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutController {

    private final UserService userService;

    public AboutController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/about")
    public String showAboutPage(Model model) {
        userService.getCurrentUser().ifPresent(u -> model.addAttribute("currentUser", u));
        return "about";
    }
}
