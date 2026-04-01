package com.friendbook.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("/home")
    public String home(@RequestParam(required = false) String q, Model model) {
        model.addAttribute("initialQuery", q == null ? "" : q);
        return "home";
    }

    @GetMapping("/profile")
    public String myProfile(Model model) {
        model.addAttribute("requestedUsername", "");
        return "profile";
    }

    @GetMapping("/users/{username}")
    public String profile(@PathVariable String username, Model model) {
        model.addAttribute("requestedUsername", username);
        return "profile";
    }
}
