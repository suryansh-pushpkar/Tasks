package com.frindbook.controller;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.frindbook.service.UserService;

@Controller
public class PageController {

    private final UserService userService;

    public PageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String showSignupPage() {
        return "signup";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/about")
    public String showAboutPage() {
        return "about";
    }

    @GetMapping("/")
    public String showIndexPage() {
        return "index";
    }

    @GetMapping("/profile/{username}")
    public String showProfilePage(@PathVariable String username, Model model) {
        return userService.findByUsername(username)
                .map(user -> {
                    model.addAttribute("user", user);
                    return "profile";
                })
                .orElse("redirect:/login?error=notfound");
    }
}
