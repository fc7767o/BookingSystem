package com.example.booking.service;

import com.example.booking.model.Room;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class RoomService {

    private final StorageService storageService;
    private List<Room> rooms = new ArrayList<>();
    private int nextId = 1;

    private static final String FILE_NAME = "rooms.json";

    public RoomService(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostConstruct
    public void init() {
        // Загружаем комнаты из файла
        rooms = storageService.loadList(FILE_NAME, Room.class);

        if (rooms.isEmpty()) {
            // Если файл пустой — добавляем тестовые номера
            rooms.add(new Room(nextId++, "Стандарт", "Уютный номер с видом на город", 3000, "/img/standard.jpg", true));
            rooms.add(new Room(nextId++, "Люкс", "Просторный номер с джакузи", 7000, "/img/lux.jpg", true));
            rooms.add(new Room(nextId++, "Семейный", "Две комнаты, идеально для семьи", 5000, "/img/family.jpg", true));
            rooms.add(new Room(nextId++, "Эконом", "Бюджетный вариант", 1500, "/img/econom.jpg", false));
            save();
            System.out.println("Созданы тестовые номера");
        } else {
            // Находим максимальный ID для nextId
            nextId = rooms.stream().mapToInt(Room::getId).max().orElse(0) + 1;
        }
    }

    private void save() {
        storageService.saveList(FILE_NAME, rooms);
    }

    public List<Room> getAllRooms() {
        return rooms;
    }

    public List<Room> getActiveRooms() {
        return rooms.stream().filter(Room::isActive).toList();
    }

    public Room getRoom(int id) {
        return rooms.stream().filter(r -> r.getId() == id).findFirst().orElse(null);
    }

    public void toggleActive(int id) {
        Room room = getRoom(id);
        if (room != null) {
            room.setActive(!room.isActive());
            save();
        }
    }

    public void setPrice(int id, double price) {
        Room room = getRoom(id);
        if (room != null) {
            room.setPrice(price);
            save();
        }
    }
}