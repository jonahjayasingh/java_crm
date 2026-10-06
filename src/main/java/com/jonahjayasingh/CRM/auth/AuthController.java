package com.jonahjayasingh.CRM.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jonahjayasingh.CRM.auth.dto.AuthResponse;
import com.jonahjayasingh.CRM.auth.dto.LoginRequest;
import com.jonahjayasingh.CRM.auth.dto.RefreshTokenRequest;
import com.jonahjayasingh.CRM.auth.dto.RegisterRequest;

import org.springframework.web.bind.annotation.RequestParam;
import com.jonahjayasingh.CRM.Config.DummyDataService;

@RestController 
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired 
    private AuthService authService;

    @Autowired
    private DummyDataService dummyDataService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
        @RequestBody RegisterRequest request
    ){
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
        @RequestBody  LoginRequest request
    ){
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
        @RequestBody RefreshTokenRequest request
    ){
        return ResponseEntity.ok(authService.refreshtoken(request));
    }

    @PostMapping ("/logout")
    public  ResponseEntity<String> logout(
        @RequestBody RefreshTokenRequest request
    ){
        authService.logout(request.getRefreshToken());
        return ResponseEntity.ok("Logged out successfully");
    }

    @PostMapping("/seed-dummy-data")
    public ResponseEntity<String> seedDummyData(
        @RequestParam(defaultValue = "true") boolean force
    ) {
        String result = dummyDataService.seedAllDummyData(force);
        return ResponseEntity.ok(result);
    }
}
