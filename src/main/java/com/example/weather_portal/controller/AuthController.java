package com.example.weather_portal.controller;

import com.example.weather_portal.model.User;
import com.example.weather_portal.service.UserService;
import com.example.weather_portal.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {
    private final UserService userService;
    private final UserRepository userRepository;

    public AuthController(UserService userService, UserRepository repo) {
        this.userService = userService;
        this.userRepository = repo;
    }

    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam(defaultValue = "ROLE_USER") String role,
                           Model model) {
        try {
            userService.register(username, password, role);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() { return "login"; }
}
