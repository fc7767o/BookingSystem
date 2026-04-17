package com.example.booking.model;

public class Client extends User {
    private String name;
    private String phone;

    public Client() {}

    public Client(int id, String login, String password, String name, String phone) {
        super(id, login, password);
        this.name = name;
        this.phone = phone;
    }

    @Override
    public String getRole() {
        return "client";
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}