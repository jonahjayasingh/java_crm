package com.jonahjayasingh.CRM.opportunity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jonahjayasingh.CRM.Customer.Customer;
import com.jonahjayasingh.CRM.Customer.CustomerRepo;
import com.jonahjayasingh.CRM.audit.AuditLogService;
import com.jonahjayasingh.CRM.exception.ResourceNotFoundException;
import com.jonahjayasingh.CRM.opportunity.dto.OpportunityRequest;
import com.jonahjayasingh.CRM.opportunity.dto.OpportunityResponse;

@Service
@Transactional
public class OpportunityService {

    @Autowired
    private OpportunityRepository opportunityRepository;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private AuditLogService auditLogService;

    public OpportunityResponse createOpportunity(OpportunityRequest request, String username) {
        Customer customer = customerRepo.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + request.getCustomerId()));

        Opportunity opportunity = new Opportunity();
        opportunity.setTitle(request.getTitle());
        opportunity.setAmount(request.getAmount());
        opportunity.setStage(request.getStage() != null ? request.getStage() : OpportunityStage.PROSPECTING);
        opportunity.setProbability(request.getProbability());
        opportunity.setExpectedCloseDate(request.getExpectedCloseDate());
        opportunity.setCustomer(customer);

        Opportunity savedOpp = opportunityRepository.save(opportunity);
        auditLogService.log("CREATE", "Opportunity", savedOpp.getId(), username, "Created opportunity: " + savedOpp.getTitle() + " ($" + savedOpp.getAmount() + ")");
        return mapToResponse(savedOpp);
    }

    public List<OpportunityResponse> getAllOpportunities() {
        return opportunityRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public OpportunityResponse getOpportunityById(Long id) {
        Opportunity opportunity = opportunityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Opportunity not found with id: " + id));
        return mapToResponse(opportunity);
    }

    public OpportunityResponse updateOpportunity(Long id, OpportunityRequest request, String username) {
        Opportunity opportunity = opportunityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Opportunity not found with id: " + id));

        opportunity.setTitle(request.getTitle());
        opportunity.setAmount(request.getAmount());
        if (request.getStage() != null) {
            opportunity.setStage(request.getStage());
        }
        opportunity.setProbability(request.getProbability());
        opportunity.setExpectedCloseDate(request.getExpectedCloseDate());

        if (request.getCustomerId() != null) {
            Customer customer = customerRepo.findById(request.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + request.getCustomerId()));
            opportunity.setCustomer(customer);
        }

        Opportunity updatedOpp = opportunityRepository.save(opportunity);
        auditLogService.log("UPDATE", "Opportunity", updatedOpp.getId(), username, "Updated opportunity: " + updatedOpp.getTitle());
        return mapToResponse(updatedOpp);
    }

    public void deleteOpportunity(Long id, String username) {
        Opportunity opportunity = opportunityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Opportunity not found with id: " + id));
        opportunityRepository.delete(opportunity);
        auditLogService.log("DELETE", "Opportunity", id, username, "Deleted opportunity: " + opportunity.getTitle());
    }

    private OpportunityResponse mapToResponse(Opportunity opp) {
        return OpportunityResponse.builder()
                .id(opp.getId())
                .title(opp.getTitle())
                .amount(opp.getAmount())
                .stage(opp.getStage())
                .probability(opp.getProbability())
                .expectedCloseDate(opp.getExpectedCloseDate())
                .customerId(opp.getCustomer() != null ? opp.getCustomer().getId() : null)
                .customerName(opp.getCustomer() != null ? opp.getCustomer().getName() : null)
                .createdAt(opp.getCreatedAt())
                .updatedAt(opp.getUpdatedAt())
                .build();
    }
}
