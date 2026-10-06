package com.jonahjayasingh.CRM.opportunity.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.jonahjayasingh.CRM.opportunity.OpportunityStage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OpportunityResponse {
    private Long id;
    private String title;
    private BigDecimal amount;
    private OpportunityStage stage;
    private Integer probability;
    private LocalDate expectedCloseDate;
    private Long customerId;
    private String customerName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
