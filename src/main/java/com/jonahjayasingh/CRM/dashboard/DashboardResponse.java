package com.jonahjayasingh.CRM.dashboard;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponse {
    private String role;
    private long totalCustomers;
    private long totalLeads;
    private long totalOpportunities;
    private long totalTasks;
    private long openTickets;
    private long totalProducts;
    private long totalQuotations;
    private long totalInvoices;
    private BigDecimal totalPipelineValue;
    private BigDecimal totalRevenueCollected;

    private long totalEvents;
    private long myAssignedLeads;
    private long myPendingTasks;
    private long myOpenTickets;
}
