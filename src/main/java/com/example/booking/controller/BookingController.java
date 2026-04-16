package com.example.booking.controller;

import com.example.booking.model.Booking;
import com.example.booking.service.BookingService;
import com.example.booking.service.RoomService;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final RoomService roomService;

    public BookingController(BookingService bookingService, RoomService roomService) {
        this.bookingService = bookingService;
        this.roomService = roomService;
    }

    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/client/{clientId}")
    public List<Booking> getClientBookings(@PathVariable int clientId) {
        return bookingService.getBookingsByClient(clientId);
    }

    @PostMapping("/create")
    public Map<String, Object> createBooking(@RequestParam int clientId,
                                             @RequestParam int roomId,
                                             @RequestParam String date) {
        double price = roomService.getRoom(roomId).getPrice();
        Booking booking = bookingService.createBooking(clientId, roomId, LocalDate.parse(date), price);
        return Map.of("status", "ok", "booking", booking);
    }

    @PostMapping("/{id}/approve")
    public Map<String, String> approveBooking(@PathVariable int id) {
        bookingService.approveBooking(id);
        return Map.of("status", "ok");
    }

    @PostMapping("/{id}/reject")
    public Map<String, String> rejectBooking(@PathVariable int id, @RequestParam String reason) {
        bookingService.rejectBooking(id, reason);
        return Map.of("status", "ok");
    }
}