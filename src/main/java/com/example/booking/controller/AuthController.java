package com.example.booking.controller;

import com.example.booking.model.User;
import com.example.booking.service.UserService;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestParam String login, @RequestParam String password) {
        User user = userService.login(login, password);
        if (user != null) {
            return Map.of("success", true, "user", user);
        }
        return Map.of("success", false, "message", "Неверный логин или пароль");
    }

    @PostMapping("/register")
    public Map<String, Object> register(@RequestParam String login,
                                        @RequestParam String password,
                                        @RequestParam String name,
                                        @RequestParam String phone) {
        User user = userService.register(login, password, name, phone);
        return Map.of("success", true, "user", user);
    }
}