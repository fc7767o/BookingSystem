package com.example.booking.model;

public class Admin extends User {
    private String fullName;

    public Admin() {}

    public Admin(int id, String login, String password, String fullName) {
        super(id, login, password);
        this.fullName = fullName;
    }

    @Override
    public String getRole() {
        return "admin";
    }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
}