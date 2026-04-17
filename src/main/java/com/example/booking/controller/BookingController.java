package com.example.booking.controller;

import com.example.booking.model.Booking;
import com.example.booking.model.Room;
import com.example.booking.model.User;
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

    /**
     * Получить все бронирования
     */
    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    /**
     * Получить ожидающие бронирования
     */
    @GetMapping("/pending")
    public List<Booking> getPendingBookings() {
        return bookingService.getPendingBookings();
    }

    /**
     * Получить бронирования клиента
     */
    @GetMapping("/client/{clientId}")
    public List<Booking> getClientBookings(@PathVariable int clientId) {
        return bookingService.getBookingsByClient(clientId);
    }

    /**
     * Создать новую заявку на бронирование
     */
    @PostMapping("/create")
    public Map<String, Object> createBooking(@RequestParam int clientId,
                                             @RequestParam int roomId,
                                             @RequestParam String date) {
        LocalDate bookingDate = LocalDate.parse(date);

        if (!bookingService.isRoomAvailable(roomId, bookingDate)) {
            return Map.of("success", false, "message", "Номер занят на эту дату");
        }

        Room room = roomService.getRoom(roomId);
        if (room == null || !room.isActive()) {
            return Map.of("success", false, "message", "Номер недоступен для бронирования");
        }

        double price = room.getPrice();
        Booking booking = bookingService.createBooking(clientId, roomId, bookingDate, price);

        return Map.of("success", true, "booking", booking);
    }

    /**
     * Одобрить бронирование
     */
    @PostMapping("/{id}/approve")
    public Map<String, Object> approveBooking(@PathVariable int id) {
        Booking booking = bookingService.getBooking(id);
        if (booking == null) {
            return Map.of("success", false, "message", "Бронирование не найдено");
        }

        bookingService.approveBooking(id);
        return Map.of("success", true, "message", "Бронирование одобрено. Требуется оплата в течение 24 часов.");
    }

    @PostMapping("/{id}/reject")
    public Map<String, Object> rejectBooking(@PathVariable int id,
                                             @RequestParam String reason) {
        Booking booking = bookingService.getBooking(id);
        if (booking == null) {
            return Map.of("success", false, "message", "Бронирование не найдено");
        }

        if (reason == null || reason.trim().isEmpty()) {
            return Map.of("success", false, "message", "Причина отказа обязательна");
        }

        bookingService.rejectBooking(id, reason);
        return Map.of("success", true, "message", "Бронирование отклонено");
    }


    @PostMapping("/{id}/pay")
    public Map<String, Object> markAsPaid(@PathVariable int id) {
        Booking booking = bookingService.getBooking(id);
        if (booking == null) {
            return Map.of("success", false, "message", "Бронирование не найдено");
        }

        if (!booking.getStatus().equals("approved")) {
            return Map.of("success", false, "message", "Можно оплатить только одобренное бронирование");
        }

        bookingService.markAsPaid(id);
        return Map.of("success", true, "message", "Оплата принята");
    }


    @GetMapping("/check-availability")
    public Map<String, Object> checkAvailability(@RequestParam int roomId,
                                                 @RequestParam String date) {
        LocalDate checkDate = LocalDate.parse(date);
        boolean available = bookingService.isRoomAvailable(roomId, checkDate);

        Room room = roomService.getRoom(roomId);
        if (room == null || !room.isActive()) {
            available = false;
        }

        return Map.of("available", available);
    }
}