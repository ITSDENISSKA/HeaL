package com.example.heal;

// User.java
public class User {
    private int id;
    private String username;
    private String password;
    private String registrationDate;

    public User(int id, String username, String password, String registrationDate) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.registrationDate = registrationDate;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }
}
