package com.jonahjayasingh.CRM.auth.dto;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
 
public class AuthResponse {
    private String accessToken;
    private  String refreshToken;
    private String tokenType;
    private  String name;
    private String role;
    
}
