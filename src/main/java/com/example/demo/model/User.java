package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "app_users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String username;
    private String password;
    private String hotel; // e.g., "Radisson", "Taj", "Lalit"
    private String role;  // "ADMIN" or "CUSTOMER"

    // Getters and Setters
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getHotel() { return hotel; }
    public void setHotel(String hotel) { this.hotel = hotel; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}