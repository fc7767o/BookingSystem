package com.example.booking.model;

public class Room {
    private int id;
    private String title;
    private String description;
    private double price;
    private String photo;  // URL или имя файла
    private boolean active;

    public Room() {}

    public Room(int id, String title, String description, double price, String photo, boolean active) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.photo = photo;
        this.active = active;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getPhoto() { return photo; }
    public void setPhoto(String photo) { this.photo = photo; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}