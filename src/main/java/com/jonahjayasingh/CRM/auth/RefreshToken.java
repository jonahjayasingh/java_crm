package com.jonahjayasingh.CRM.auth;

import java.time.Instant;

import com.jonahjayasingh.CRM.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity 
@Table(name = "refresh_tokens")
@Data 
public class RefreshToken {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column (nullable = false, unique = true,length = 500)
    private String token;

    @Column (nullable = false)
    private Instant expiryDate;

    @OneToOne 
    @JoinColumn (name = "user_id",nullable = false)
    private User user;
    
}
