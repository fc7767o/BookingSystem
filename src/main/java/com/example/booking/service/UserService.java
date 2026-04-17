package com.example.booking.service;

import com.example.booking.model.*;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class UserService {
    private List<User> users = new ArrayList<>();
    private int nextId = 1;

    public UserService() {
        users.add(new Admin(nextId++, "admin", "12345", "Администратор Системы"));
        users.add(new Client(nextId++, "client", "123", "Клиент", "89996529322"));
    }

    public User login(String login, String password) {
        return users.stream()
                .filter(u -> u.getLogin().equals(login) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    public Client registerClient(String login, String password, String name, String phone) {
        Client client = new Client(nextId++, login, password, name, phone);
        users.add(client);
        return client;
    }

    public User getUser(int id) {
        return users.stream().filter(u -> u.getId() == id).findFirst().orElse(null);
    }
}