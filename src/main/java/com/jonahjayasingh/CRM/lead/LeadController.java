package com.jonahjayasingh.CRM.lead;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jonahjayasingh.CRM.lead.dto.LeadRequest;
import com.jonahjayasingh.CRM.lead.dto.LeadResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/leads")
public class LeadController {

    @Autowired
    private LeadService leadService;

    @PostMapping
    public ResponseEntity<LeadResponse> createLead(@Valid @RequestBody LeadRequest request, Authentication authentication) {
        String username = authentication != null ? authentication.getName() : "ANONYMOUS";
        return ResponseEntity.status(HttpStatus.CREATED).body(leadService.createLead(request, username));
    }

    @GetMapping
    public ResponseEntity<List<LeadResponse>> getAllLeads() {
        return ResponseEntity.ok(leadService.getAllLeads());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeadResponse> getLeadById(@PathVariable Long id) {
        return ResponseEntity.ok(leadService.getLeadById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LeadResponse> updateLead(@PathVariable Long id, @Valid @RequestBody LeadRequest request, Authentication authentication) {
        String username = authentication != null ? authentication.getName() : "ANONYMOUS";
        return ResponseEntity.ok(leadService.updateLead(id, request, username));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLead(@PathVariable Long id, Authentication authentication) {
        String username = authentication != null ? authentication.getName() : "ANONYMOUS";
        leadService.deleteLead(id, username);
        return ResponseEntity.ok("Lead deleted successfully with ID: " + id);
    }
}
