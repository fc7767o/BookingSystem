package com.example.booking.service;

import com.example.booking.model.Booking;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class BookingService {
    private List<Booking> bookings = new ArrayList<>();
    private int nextId = 1;

    public List<Booking> getAllBookings() {
        return bookings;
    }

    public List<Booking> getBookingsByClient(int clientId) {
        return bookings.stream().filter(b -> b.getClientId() == clientId).toList();
    }

    public Booking createBooking(int clientId, int roomId, LocalDate date, double price) {
        Booking booking = new Booking(nextId++, clientId, roomId, date, "pending", price);
        bookings.add(booking);
        return booking;
    }

    public void approveBooking(int id) {
        for (Booking b : bookings) {
            if (b.getId() == id) {
                b.setStatus("approved");
                return;
            }
        }
    }

    public void rejectBooking(int id, String reason) {
        for (Booking b : bookings) {
            if (b.getId() == id) {
                b.setStatus("rejected");
                b.setReason(reason);
                return;
            }
        }
    }
}