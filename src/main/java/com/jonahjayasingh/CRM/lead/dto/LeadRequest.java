package com.jonahjayasingh.CRM.lead.dto;

import com.jonahjayasingh.CRM.lead.LeadStatus;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LeadRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email address")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Phone is required")
    private String phone;

    private String company;

    private String source;

    private LeadStatus status;

    private String notes;

    private Long assignedToUserId;
}
