package com.th.controller;

import com.th.modal.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    @GetMapping("variable-expression")
    public String VariableExpression(Model model) {
        User user = new User();
        user.setUsername("thymeleaf");
        user.setPassword("thymeleaf@123");
        user.setEmail("thymeleaf@example.com");
        user.setRole("USER");
        model.addAttribute("user", user);
        return "variable-expression";
    }

}
