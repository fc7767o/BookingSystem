package com.example.booking.service;

import com.example.booking.model.Room;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class RoomService {
    private List<Room> rooms = new ArrayList<>();
    private int nextId = 1;

    public RoomService() {
        rooms.add(new Room(nextId++, "Стандарт", "Номер с видом на город", 3000, "/img/standard.jpg", true));
        rooms.add(new Room(nextId++, "Люкс", "Номер с джакузи", 7000, "/img/lux.jpg", true));
        rooms.add(new Room(nextId++, "Семейный", "Две комнаты", 5000, "/img/family.jpg", true));
        rooms.add(new Room(nextId++, "Эконом", "Бюджетный вариант", 1500, "/img/econom.jpg", false));
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

    public void updateRoom(int id, Room updatedRoom) {
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getId() == id) {
                updatedRoom.setId(id);
                rooms.set(i, updatedRoom);
                return;
            }
        }
    }

    public void toggleActive(int id) {
        Room room = getRoom(id);
        if (room != null) {
            room.setActive(!room.isActive());
        }
    }

    public void setPrice(int id, double price) {
        Room room = getRoom(id);
        if (room != null) {
            room.setPrice(price);
        }
    }
}