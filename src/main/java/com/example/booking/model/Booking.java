package com.example.booking.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Booking {

    private int id;
    private int clientId;
    private int roomId;
    private LocalDate date;
    private String status;
    private double price;
    private String reason;
    private LocalDateTime createdAt;

    public Booking() {
        this.createdAt = LocalDateTime.now();
    }

    public Booking(int id, int clientId, int roomId, LocalDate date, double price) {
        this.id = id;
        this.clientId = clientId;
        this.roomId = roomId;
        this.date = date;
        this.price = price;
        this.status = "pending";
        this.createdAt = LocalDateTime.now();
    }

    public void approve() {
        this.status = "approved";
    }

    public void reject(String reason) {
        this.status = "rejected";
        this.reason = reason;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getClientId() { return clientId; }
    public void setClientId(int clientId) { this.clientId = clientId; }

    public int getRoomId() { return roomId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}