package com.jonahjayasingh.CRM.opportunity.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.jonahjayasingh.CRM.opportunity.OpportunityStage;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OpportunityRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Amount is required")
    @Min(value = 0, message = "Amount must be positive")
    private BigDecimal amount;

    private OpportunityStage stage;

    private Integer probability;

    private LocalDate expectedCloseDate;

    @NotNull(message = "Customer ID is required")
    private Long customerId;
}
