package com.example.booking.service;

import com.example.booking.model.Booking;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class BookingService {

    private final StorageService storageService;
    private List<Booking> bookings = new ArrayList<>();
    private int nextId = 1;

    private static final String FILE_NAME = "bookings.json";

    public BookingService(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostConstruct
    public void init() {
        bookings = storageService.loadList(FILE_NAME, Booking.class);

        if (!bookings.isEmpty()) {
            nextId = bookings.stream().mapToInt(Booking::getId).max().orElse(0) + 1;
        }

        System.out.println("Загружено бронирований: " + bookings.size());
    }

    private void save() {
        storageService.saveList(FILE_NAME, bookings);
    }

    public List<Booking> getAllBookings() {
        return bookings;
    }

    public List<Booking> getBookingsByClient(int clientId) {
        return bookings.stream().filter(b -> b.getClientId() == clientId).toList();
    }

    public List<Booking> getBookingsByStatus(String status) {
        return bookings.stream().filter(b -> b.getStatus().equals(status)).toList();
    }

    public List<Booking> getPendingBookings() {
        return getBookingsByStatus("pending");
    }

    public Booking createBooking(int clientId, int roomId, LocalDate date, double price) {
        Booking booking = new Booking(nextId++, clientId, roomId, date, price);
        bookings.add(booking);
        save();
        System.out.println("Создана заявка #" + booking.getId());
        return booking;
    }

    public void approveBooking(int id) {
        Booking booking = findBooking(id);
        if (booking != null) {
            booking.approve();
            save();
            System.out.println("Заявка #" + id + " одобрена");
        }
    }

    public void rejectBooking(int id, String reason) {
        Booking booking = findBooking(id);
        if (booking != null) {
            booking.reject(reason);
            save();
            System.out.println("Заявка #" + id + " отклонена");
        }
    }

    public void markAsPaid(int id) {
        Booking booking = findBooking(id);
        if (booking != null) {
            booking.setStatus("paid");
            save();
            System.out.println("Заявка #" + id + " оплачена");
        }
    }

    @Scheduled(fixedRate = 1800000) // 60000
    public void autoExpireBookings() {
        LocalDateTime now = LocalDateTime.now();
        boolean changed = false;

        for (Booking b : bookings) {
            if (b.getStatus().equals("approved")) {
                if (b.getCreatedAt().plusHours(24).isBefore(now)) { //plusMinutes(1)
                    b.setStatus("expired");
                    changed = true;
                    System.out.println("Бронь #" + b.getId() + " просрочена");
                }
            }
        }

        if (changed) {
            save();
        }
    }

    public boolean isRoomAvailable(int roomId, LocalDate date) {
        return bookings.stream()
                .filter(b -> b.getRoomId() == roomId)
                .filter(b -> b.getDate().equals(date))
                .filter(b -> !b.getStatus().equals("rejected"))
                .filter(b -> !b.getStatus().equals("expired"))
                .findAny()
                .isEmpty();
    }

    private Booking findBooking(int id) {
        return bookings.stream().filter(b -> b.getId() == id).findFirst().orElse(null);
    }

    public Booking getBooking(int id) {
        return findBooking(id);
    }
}