package com.jonahjayasingh.CRM.security;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jonahjayasingh.CRM.user.User;
import com.jonahjayasingh.CRM.user.UserRepository;

@Service 
public class MyUserDetailsService implements UserDetailsService{
    private final UserRepository userRepository;

    public MyUserDetailsService(
        UserRepository userRepository
    ){
        this.userRepository = userRepository;
    }

    @Override 
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        User user = userRepository
                    .findByName(username)
                    .orElseThrow(() -> 
                        new UsernameNotFoundException("User not found")
                );

        return new org.springframework.security.core.userdetails.User(
            user.getName(),
            user.getPassword(),
            user.isEnabled(),
            true,
            true,
            true,
            List.of(
                new SimpleGrantedAuthority(
                    "ROLE_" + user.getRole().name()
                )
            )
        );
    }
}
