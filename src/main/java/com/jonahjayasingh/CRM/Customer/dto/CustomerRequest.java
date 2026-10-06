package com.jonahjayasingh.CRM.Customer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data 
@AllArgsConstructor 
public class CustomerRequest {

    @NotBlank (message = "Name is required")
    @Size (max = 500,message = "Name cannot exceed 500 characters")
    private String name;

    @NotBlank (message = "Phone number is required")
    private String phoneNo;

    private String collegeName;

    @NotNull (message = "Interested status is required")
    private Boolean isInterested;

    @Size (max = 10000,message = "Note cannot exceed 10000 characters")
    private String note;

}
