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
        rooms = storageService.loadList(FILE_NAME, Room.class);

        if (rooms.isEmpty()) {
            rooms.add(new Room(nextId++, "Стандарт", "Уютный номер с видом на город", 3000, "/images/rooms/папап.jpg", true));
            rooms.add(new Room(nextId++, "Люкс", "Просторный номер с джакузи", 7000, "/images/rooms/йцйцй.webp", true));
            rooms.add(new Room(nextId++, "Семейный", "Две комнаты", 5000, "/images/rooms/OIP.webp", true));
            rooms.add(new Room(nextId++, "Эконом", "Бюджетный вариант", 1500, "/images/rooms/helo.jpg", false));
            save();
            System.out.println("Созданы тестовые номера");
        } else {
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