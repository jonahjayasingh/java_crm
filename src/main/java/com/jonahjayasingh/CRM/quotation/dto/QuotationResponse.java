package com.jonahjayasingh.CRM.quotation.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.jonahjayasingh.CRM.quotation.QuotationStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuotationResponse {
    private Long id;
    private String quotationNumber;
    private Long customerId;
    private String customerName;
    private BigDecimal totalAmount;
    private QuotationStatus status;
    private LocalDate validUntil;
    private String termsAndConditions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
