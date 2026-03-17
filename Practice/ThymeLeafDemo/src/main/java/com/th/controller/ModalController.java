package com.th.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/model")
public class ModalController {
    @GetMapping
    public Model Hello(Model model) {
        model.addAttribute("message", "Hello World from Spring MVC Thymeleaf");
        return model;
    }
}
