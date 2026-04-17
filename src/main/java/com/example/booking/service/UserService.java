package com.example.booking.service;

import com.example.booking.model.*;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class UserService {

    private final StorageService storageService;
    private List<User> users = new ArrayList<>();
    private int nextId = 1;

    private static final String FILE_NAME = "users.json";

    public UserService(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostConstruct
    public void init() {
        List<?> loaded = storageService.loadList(FILE_NAME, Object.class);

        for (Object obj : loaded) {
            Map<String, Object> map = (Map<String, Object>) obj;
            String role = (String) map.get("role");

            if ("admin".equals(role)) {
                Admin admin = new Admin();
                admin.setId((Integer) map.get("id"));
                admin.setLogin((String) map.get("login"));
                admin.setPassword((String) map.get("password"));
                admin.setFullName((String) map.get("fullName"));
                users.add(admin);
            } else {
                Client client = new Client();
                client.setId((Integer) map.get("id"));
                client.setLogin((String) map.get("login"));
                client.setPassword((String) map.get("password"));
                client.setName((String) map.get("name"));
                client.setPhone((String) map.get("phone"));
                users.add(client);
            }
        }

        if (users.isEmpty()) {
            users.add(new Admin(nextId++, "admin", "12345", "Администратор"));
            save();
            System.out.println("✅ Создан админ: admin / 12345");
        } else {
            nextId = users.stream().mapToInt(User::getId).max().orElse(0) + 1;
        }

        System.out.println("📂 Загружено пользователей: " + users.size());
    }

    private void save() {
        storageService.saveList(FILE_NAME, users);
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
        save();
        return client;
    }

    public User getUser(int id) {
        return users.stream().filter(u -> u.getId() == id).findFirst().orElse(null);
    }
}