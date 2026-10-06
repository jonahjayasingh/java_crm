package com.jonahjayasingh.CRM.lead;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jonahjayasingh.CRM.audit.AuditLogService;
import com.jonahjayasingh.CRM.exception.ResourceNotFoundException;
import com.jonahjayasingh.CRM.lead.dto.LeadRequest;
import com.jonahjayasingh.CRM.lead.dto.LeadResponse;
import com.jonahjayasingh.CRM.user.User;
import com.jonahjayasingh.CRM.user.UserRepository;

@Service
@Transactional
public class LeadService {

    @Autowired
    private LeadRepository leadRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuditLogService auditLogService;

    public LeadResponse createLead(LeadRequest request, String username) {
        Lead lead = new Lead();
        lead.setName(request.getName());
        lead.setEmail(request.getEmail());
        lead.setPhone(request.getPhone());
        lead.setCompany(request.getCompany());
        lead.setSource(request.getSource());
        lead.setNotes(request.getNotes());
        lead.setStatus(request.getStatus() != null ? request.getStatus() : LeadStatus.NEW);

        if (request.getAssignedToUserId() != null) {
            User user = userRepository.findById(request.getAssignedToUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getAssignedToUserId()));
            lead.setAssignedTo(user);
        }

        Lead savedLead = leadRepository.save(lead);
        auditLogService.log("CREATE", "Lead", savedLead.getId(), username, "Created lead: " + savedLead.getName());
        return mapToResponse(savedLead);
    }

    public List<LeadResponse> getAllLeads() {
        return leadRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public LeadResponse getLeadById(Long id) {
        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lead not found with id: " + id));
        return mapToResponse(lead);
    }

    public LeadResponse updateLead(Long id, LeadRequest request, String username) {
        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lead not found with id: " + id));

        lead.setName(request.getName());
        lead.setEmail(request.getEmail());
        lead.setPhone(request.getPhone());
        lead.setCompany(request.getCompany());
        lead.setSource(request.getSource());
        lead.setNotes(request.getNotes());

        if (request.getStatus() != null) {
            lead.setStatus(request.getStatus());
        }

        if (request.getAssignedToUserId() != null) {
            User user = userRepository.findById(request.getAssignedToUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getAssignedToUserId()));
            lead.setAssignedTo(user);
        }

        Lead updatedLead = leadRepository.save(lead);
        auditLogService.log("UPDATE", "Lead", updatedLead.getId(), username, "Updated lead: " + updatedLead.getName());
        return mapToResponse(updatedLead);
    }

    public void deleteLead(Long id, String username) {
        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lead not found with id: " + id));
        leadRepository.delete(lead);
        auditLogService.log("DELETE", "Lead", id, username, "Deleted lead: " + lead.getName());
    }

    private LeadResponse mapToResponse(Lead lead) {
        return LeadResponse.builder()
                .id(lead.getId())
                .name(lead.getName())
                .email(lead.getEmail())
                .phone(lead.getPhone())
                .company(lead.getCompany())
                .source(lead.getSource())
                .status(lead.getStatus())
                .notes(lead.getNotes())
                .createdAt(lead.getCreatedAt())
                .updatedAt(lead.getUpdatedAt())
                .assignedToUserId(lead.getAssignedTo() != null ? lead.getAssignedTo().getId() : null)
                .assignedToUserName(lead.getAssignedTo() != null ? lead.getAssignedTo().getName() : null)
                .build();
    }
}
