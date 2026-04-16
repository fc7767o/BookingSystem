package com.example.booking.service;

import com.example.booking.model.User;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class UserService {
    private List<User> users = new ArrayList<>();
    private int nextId = 1;

    public UserService() {
        users.add(new User(nextId++, "admin", "12345", "admin", "Администратор", ""));
    }

    public User login(String login, String password) {
        return users.stream()
                .filter(u -> u.getLogin().equals(login) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    public User register(String login, String password, String name, String phone) {
        User user = new User(nextId++, login, password, "client", name, phone);
        users.add(user);
        return user;
    }

    public User getUser(int id) {
        return users.stream().filter(u -> u.getId() == id).findFirst().orElse(null);
    }
}