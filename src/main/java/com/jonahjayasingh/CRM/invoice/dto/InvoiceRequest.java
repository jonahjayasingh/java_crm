package com.jonahjayasingh.CRM.invoice.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.jonahjayasingh.CRM.invoice.InvoiceStatus;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InvoiceRequest {

    @NotBlank(message = "Invoice number is required")
    private String invoiceNumber;

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotNull(message = "Amount is required")
    @Min(value = 0, message = "Amount cannot be negative")
    private BigDecimal amount;

    private InvoiceStatus status;

    private LocalDate issueDate;

    private LocalDate dueDate;
}
