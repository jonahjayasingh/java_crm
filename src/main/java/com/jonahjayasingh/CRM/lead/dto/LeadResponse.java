package com.jonahjayasingh.CRM.lead.dto;

import java.time.LocalDateTime;

import com.jonahjayasingh.CRM.lead.LeadStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LeadResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String company;
    private String source;
    private LeadStatus status;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long assignedToUserId;
    private String assignedToUserName;
}
