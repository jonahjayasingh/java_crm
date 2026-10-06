package com.jonahjayasingh.CRM.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service 
public class JwtService {
    
    @Value ("${jwt.secret}")
    private String secret;

    @Value ("${jwt.expiration}")
    private long jwtExpiration;


    private SecretKey getSigningKey(){
        return Keys.hmacShaKeyFor(
            secret.getBytes(StandardCharsets.UTF_8)
        );
    }

    public String genrateToken(UserDetails userDetails){
        Map<String,Object> claims = new HashMap<>();
        return Jwts.builder()
                    .claims( claims)
                    .subject(userDetails.getUsername())
                    .issuedAt(new Date())
                    .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                    .signWith(getSigningKey())
                    .compact();
    }

    public <T> T extractClaim(
        String token,
        Function<Claims,T> resolver
    ){
        Claims claims = Jwts.parser()
                            .verifyWith(getSigningKey())
                            .build()
                            .parseSignedClaims(token)
                            .getPayload();
        
        return resolver.apply(claims);
    }

    public String extractUsername(String token){
        return extractClaim(token,Claims::getSubject);
    }

    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }


    public boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public boolean isTokenValid(String token,UserDetails userDetails){
        String username = extractUsername(token);

        return username.equals(userDetails.getUsername()) && !isTokenExpired(token) ;
    }

}
