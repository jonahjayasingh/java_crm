package com.jonahjayasingh.CRM.ticket.dto;

import java.time.LocalDateTime;

import com.jonahjayasingh.CRM.ticket.TicketPriority;
import com.jonahjayasingh.CRM.ticket.TicketStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketResponse {
    private Long id;
    private String subject;
    private String description;
    private TicketStatus status;
    private TicketPriority priority;
    private Long customerId;
    private String customerName;
    private Long assignedToUserId;
    private String assignedToUserName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
