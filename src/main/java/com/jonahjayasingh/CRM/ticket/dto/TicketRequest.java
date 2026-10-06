package com.jonahjayasingh.CRM.ticket.dto;

import com.jonahjayasingh.CRM.ticket.TicketPriority;
import com.jonahjayasingh.CRM.ticket.TicketStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TicketRequest {

    @NotBlank(message = "Subject is required")
    private String subject;

    @NotBlank(message = "Description is required")
    private String description;

    private TicketStatus status;

    private TicketPriority priority;

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    private Long assignedToUserId;
}
