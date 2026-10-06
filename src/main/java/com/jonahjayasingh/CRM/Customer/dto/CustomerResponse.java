package com.jonahjayasingh.CRM.Customer.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponse {
    private Long id;
    private String name;
    private String phoneNo;
    private String collegeName;
    private Boolean isInterested;
    private String note;
    private LocalDate createAt;
    private LocalDate updateAt;
    private Long userId;
    private String userName;
}
