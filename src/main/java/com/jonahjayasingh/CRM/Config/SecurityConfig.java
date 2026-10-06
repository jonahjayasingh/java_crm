package com.jonahjayasingh.CRM.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.jonahjayasingh.CRM.security.JwtAuthenticationFilter;

@Configuration  
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired 
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean 
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean 
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean 
    public SecurityFilterChain securityFilterChain(
        HttpSecurity http
    ) throws Exception {
        http.cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(
                auth -> auth
                    // Public / Authentication endpoints
                    .requestMatchers("/api/auth/**").permitAll()

                    // Admin-only Endpoints
                    .requestMatchers("/api/audit-logs/**").hasRole("ADMIN")

                    // Admin & Manager Level User Management Endpoints
                    .requestMatchers("/api/users/**").hasAnyRole("ADMIN", "MANAGER")

                    // Commercial & Pipeline Endpoints (Admin, Manager, Sales)
                    .requestMatchers("/api/leads/**").hasAnyRole("ADMIN", "MANAGER", "SALES")
                    .requestMatchers("/api/opportunities/**").hasAnyRole("ADMIN", "MANAGER", "SALES")
                    .requestMatchers("/api/quotations/**").hasAnyRole("ADMIN", "MANAGER", "SALES")
                    .requestMatchers("/api/invoices/**").hasAnyRole("ADMIN", "MANAGER", "SALES")

                    // General Operational CRM Modules (Admin, Manager, Sales, Employee)
                    .requestMatchers("/api/customers/**").hasAnyRole("ADMIN", "MANAGER", "SALES", "EMPLOYEE")
                    .requestMatchers("/api/tasks/**").hasAnyRole("ADMIN", "MANAGER", "SALES", "EMPLOYEE")
                    .requestMatchers("/api/events/**").hasAnyRole("ADMIN", "MANAGER", "SALES", "EMPLOYEE")
                    .requestMatchers("/api/tickets/**").hasAnyRole("ADMIN", "MANAGER", "SALES", "EMPLOYEE")
                    .requestMatchers("/api/products/**").hasAnyRole("ADMIN", "MANAGER", "SALES", "EMPLOYEE")
                    .requestMatchers("/api/dashboard/**").hasAnyRole("ADMIN", "MANAGER", "SALES", "EMPLOYEE")
                    .requestMatchers(
            "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**"
                    ).permitAll()
                    // Fallback requirement for any unmapped endpoints
                    .anyRequest().authenticated()
            )
            .httpBasic(httpbasic -> httpbasic.disable())
            .formLogin(formLogin -> formLogin.disable())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
