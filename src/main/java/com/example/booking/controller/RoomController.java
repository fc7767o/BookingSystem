package com.example.booking.controller;

import com.example.booking.model.Room;
import com.example.booking.service.RoomService;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("/active")
    public List<Room> getActiveRooms() {
        return roomService.getActiveRooms();
    }

    @PostMapping("/{id}/toggle")
    public Map<String, String> toggleActive(@PathVariable int id) {
        roomService.toggleActive(id);
        return Map.of("status", "ok");
    }

    @PostMapping("/{id}/price")
    public Map<String, String> setPrice(@PathVariable int id, @RequestParam double price) {
        roomService.setPrice(id, price);
        return Map.of("status", "ok");
    }
}