package com.example.booking.service;

import com.example.booking.model.Booking;
import com.example.booking.model.Room;
import com.example.booking.model.User;
import com.example.booking.model.Client;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class BookingService {
    private List<Booking> bookings = new ArrayList<>();
    private int nextId = 1;

    /**
     * Получить все бронирования (для админа)
     */
    public List<Booking> getAllBookings() {
        return bookings;
    }

    /**
     * Получить бронирования конкретного клиента
     */
    public List<Booking> getBookingsByClient(int clientId) {
        return bookings.stream()
                .filter(b -> b.getClientId() == clientId)
                .toList();
    }

    /**
     * Получить бронирования по статусу
     */
    public List<Booking> getBookingsByStatus(String status) {
        return bookings.stream()
                .filter(b -> b.getStatus().equals(status))
                .toList();
    }

    /**
     * Получить ожидающие бронирования (для админа)
     */
    public List<Booking> getPendingBookings() {
        return getBookingsByStatus("pending");
    }

    /**
     * Создать новую заявку на бронирование
     * Цена фиксируется на момент создания
     */
    public Booking createBooking(int clientId, int roomId, LocalDate date, double price) {
        Booking booking = new Booking(nextId++, clientId, roomId, date, price);
        bookings.add(booking);
        return booking;
    }

    /**
     * Одобрить бронирование (метод делегирует в Booking)
     */
    public void approveBooking(int id) {
        Booking booking = findBooking(id);
        if (booking != null) {
            booking.approve();
        }
    }

    /**
     * Отклонить бронирование с причиной (метод делегирует в Booking)
     */
    public void rejectBooking(int id, String reason) {
        Booking booking = findBooking(id);
        if (booking != null) {
            booking.reject(reason);
        }
    }

    /**
     * Отметить как оплаченное
     */
    public void markAsPaid(int id) {
        Booking booking = findBooking(id);
        if (booking != null) {
            booking.setStatus("paid");
        }
    }

    /**
     * Проверить и сбросить просроченные брони (24 часа не оплачены)
     */
    public void expireUnpaidBookings() {
        LocalDateTime now = LocalDateTime.now();
        for (Booking b : bookings) {
            if (b.getStatus().equals("approved")) {
                if (b.getCreatedAt().plusHours(24).isBefore(now)) {
                    b.setStatus("expired");
                }
            }
        }
    }

    /**
     * Найти бронирование по ID
     */
    private Booking findBooking(int id) {
        return bookings.stream()
                .filter(b -> b.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Получить бронирование по ID (публичный метод)
     */
    public Booking getBooking(int id) {
        return findBooking(id);
    }

    /**
     * Проверить, свободен ли номер на дату
     */
    public boolean isRoomAvailable(int roomId, LocalDate date) {
        return bookings.stream()
                .filter(b -> b.getRoomId() == roomId)
                .filter(b -> b.getDate().equals(date))
                .filter(b -> !b.getStatus().equals("rejected"))
                .filter(b -> !b.getStatus().equals("expired"))
                .findAny()
                .isEmpty();
    }

    /**
     * Автоматически проверяет и истекает неоплаченные брони каждые 30 минут
//     */
    @Scheduled(fixedRate = 1800000) // 60000
    public void autoExpireBookings() {
        LocalDateTime now = LocalDateTime.now();
        int expiredCount = 0;

        for (Booking b : bookings) {
            if (b.getStatus().equals("approved")) {
                if (b.getCreatedAt().plusHours(24).isBefore(now)) { //plusMinutes(1)
                    b.setStatus("expired");
                    expiredCount++;
                }
            }
        }

        if (expiredCount > 0) {
            System.out.println("Авто-истечение: " + expiredCount + " броней переведены в expired");
        }
    }
}