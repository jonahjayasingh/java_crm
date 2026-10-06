package com.jonahjayasingh.CRM.Customer;

import java.time.LocalDate;

import com.jonahjayasingh.CRM.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity 
@Data 
@Table (name = "customer")
public class Customer {
    
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false, unique = false,length = 500)
    private  String name ;

    @Column (nullable = false,unique = false)
    private String phoneNo;

    @Column (nullable = true)
    private String collegeName;

    @Column (nullable = false)
    private Boolean isInterested = false;

    @Column (nullable = false,length = 10000)
    private String note;

    @Column (nullable = false)
    private LocalDate createAt;

    @Column (nullable = false)
    private LocalDate updateAt;

    @ManyToOne 
    @JoinColumn (name = "user_id",nullable = false)
    private User user;

    @jakarta.persistence.PrePersist
    protected void onCreate() {
        if (createAt == null) {
            createAt = LocalDate.now();
        }
        updateAt = LocalDate.now();
    }

    @jakarta.persistence.PreUpdate
    protected void onUpdate() {
        updateAt = LocalDate.now();
    }
}
