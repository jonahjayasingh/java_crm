package com.jonahjayasingh.CRM.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jonahjayasingh.CRM.audit.AuditLogService;
import com.jonahjayasingh.CRM.exception.ResourceNotFoundException;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuditLogService auditLogService;

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return mapToResponse(user);
    }

    public UserResponse updateUserRole(Long id, Role role, String performer) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        user.setRole(role);
        User updated = userRepository.save(user);
        auditLogService.log("UPDATE", "UserRole", updated.getId(), performer, "Updated user role for " + updated.getName() + " to " + role);
        return mapToResponse(updated);
    }

    public UserResponse toggleUserStatus(Long id, String performer) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        user.setEnabled(!user.isEnabled());
        User updated = userRepository.save(user);
        auditLogService.log("UPDATE", "UserStatus", updated.getId(), performer, "Toggled user status for " + updated.getName() + " to " + (updated.isEnabled() ? "ENABLED" : "DISABLED"));
        return mapToResponse(updated);
    }

    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .enabled(user.isEnabled())
                .build();
    }
}
