package com.jonahjayasingh.CRM.invoice.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.jonahjayasingh.CRM.invoice.InvoiceStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceResponse {
    private Long id;
    private String invoiceNumber;
    private Long customerId;
    private String customerName;
    private BigDecimal amount;
    private InvoiceStatus status;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
