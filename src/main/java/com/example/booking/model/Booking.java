package com.example.booking.model;

import java.time.LocalDate;

public class Booking {
    private int id;
    private int clientId;
    private int roomId;
    private LocalDate date;
    private String status;  // pending, approved, rejected, paid
    private double price;   // зафиксированная цена
    private String reason;  // причина отказа

    public Booking() {}

    public Booking(int id, int clientId, int roomId, LocalDate date, String status, double price) {
        this.id = id;
        this.clientId = clientId;
        this.roomId = roomId;
        this.date = date;
        this.status = status;
        this.price = price;
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
}