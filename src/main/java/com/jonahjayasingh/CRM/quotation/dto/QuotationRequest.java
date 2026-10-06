package com.jonahjayasingh.CRM.quotation.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.jonahjayasingh.CRM.quotation.QuotationStatus;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class QuotationRequest {

    @NotBlank(message = "Quotation number is required")
    private String quotationNumber;

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotNull(message = "Total amount is required")
    @Min(value = 0, message = "Total amount cannot be negative")
    private BigDecimal totalAmount;

    private QuotationStatus status;

    private LocalDate validUntil;

    private String termsAndConditions;
}
