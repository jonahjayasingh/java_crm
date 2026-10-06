package com.jonahjayasingh.CRM.auth;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jonahjayasingh.CRM.auth.dto.AuthResponse;
import com.jonahjayasingh.CRM.auth.dto.LoginRequest;
import com.jonahjayasingh.CRM.auth.dto.RefreshTokenRequest;
import com.jonahjayasingh.CRM.auth.dto.RegisterRequest;
import com.jonahjayasingh.CRM.security.JwtService;
import com.jonahjayasingh.CRM.user.Role;
import com.jonahjayasingh.CRM.user.User;
import com.jonahjayasingh.CRM.user.UserRepository;

import org.springframework.transaction.annotation.Transactional;

@Service 
@Transactional
public class AuthService {
    
    @Autowired 
    private UserRepository userRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired  
    private JwtService jwtService;

    private static final long REFRESH_TOKEN_DURATION = 7L*24*60*60;

    private String createRefreshToken(User user) {
        RefreshToken refreshToken = refreshTokenRepository.findByUser(user)
                .orElseGet(() -> {
                    RefreshToken token = new RefreshToken();
                    token.setUser(user);
                    return token;
                });

        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusSeconds(REFRESH_TOKEN_DURATION));

        return refreshTokenRepository.save(refreshToken).getToken();
    }

    public AuthResponse register(RegisterRequest request){
        if (request == null || request.getName() == null || request.getName().trim().isEmpty() ||
            request.getEmail() == null || request.getEmail().trim().isEmpty() ||
            request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new RuntimeException("Username, email, and password cannot be empty");
        }

        if(userRepository.existsByName(request.getName())){
            throw new RuntimeException("Username already exists");
        }

        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(
            passwordEncoder.encode(request.getPassword())
        );
        user.setRole(Role.EMPLOYEE);
        user.setEnabled(true);
        userRepository.save(user);

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
            user.getName(),
            user.getPassword(),
            user.isEnabled(),
            true,
            true,
            true,
            List.of(
                new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_"+user.getRole().name())
            )
        );
        String accessToken = jwtService.genrateToken(userDetails);

        String refreshToken = createRefreshToken(user);

        return new AuthResponse(
            accessToken,
            refreshToken,
            "Bearer",
            user.getName(),
            user.getRole().name()
        );

    }
    public AuthResponse login(
            LoginRequest request
    ) {
        if (request == null || request.getName() == null || request.getName().trim().isEmpty() ||
            request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new RuntimeException("Username and password cannot be empty");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getName(),
                        request.getPassword()
                )
        );

        User user = userRepository
                .findByName(request.getName())
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        )
                );

        UserDetails userDetails =
                new org.springframework.security.core.userdetails.User(
                        user.getName(),
                        user.getPassword(),
                        user.isEnabled(),
                        true,
                        true,
                        true,
                        java.util.List.of(
                                new org.springframework.security.core.authority.SimpleGrantedAuthority(
                                        "ROLE_" + user.getRole().name()
                                )
                        )
                );

        String accessToken =
                jwtService.genrateToken(userDetails);

        String refreshToken =
                createRefreshToken(user);

        return new AuthResponse(
                accessToken,
                refreshToken,
                "Bearer",
                user.getName(),
                user.getRole().name()
        );
    }

    public AuthResponse refreshtoken(RefreshTokenRequest request){
        RefreshToken refreshToken = refreshTokenRepository.findByToken(
            request.getRefreshToken()
        ).orElseThrow(() -> new RuntimeException("Invalid refresh token"));
        if(refreshToken.getExpiryDate().isBefore(Instant.now())){
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token expired");
        }

        User user = refreshToken.getUser();

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
            user.getName(),
            user.getPassword(),
            user.isEnabled(),
            true,
            true,
            true,
            List.of(
                new org.springframework.security.core.authority.SimpleGrantedAuthority(
                    "ROLE_"+ user.getRole().name()
                )
            )
        );

        String newAccessToken = jwtService.genrateToken(userDetails);

        return new AuthResponse(
            newAccessToken,
            refreshToken.getToken(),
            "Bearer",
            user.getName(),
            user.getRole().name()
        );
    }

   @Transactional
public void logout(String refreshToken) {

    Optional<RefreshToken> token =
            refreshTokenRepository.findByToken(refreshToken);

    if (token.isPresent()) {

        System.out.println("Token found: " + token.get().getToken());

        refreshTokenRepository.delete(token.get());

        System.out.println("Token deleted");

    } else {

        throw new RuntimeException("Token not Found");

    }
}


}
