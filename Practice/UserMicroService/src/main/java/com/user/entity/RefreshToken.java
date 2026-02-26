package com.user.entity;

import jakarta.persistence.Entity;

import java.time.Instant;

import jakarta.persistence.*;
import jakarta.persistence.Id;

@Entity
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private String token;
    private Instant expiryDate;
    
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    
    // Getters and Setters
}