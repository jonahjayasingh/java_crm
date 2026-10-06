package com.jonahjayasingh.CRM.opportunity;

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

import com.jonahjayasingh.CRM.opportunity.dto.OpportunityRequest;
import com.jonahjayasingh.CRM.opportunity.dto.OpportunityResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/opportunities")
public class OpportunityController {

    @Autowired
    private OpportunityService opportunityService;

    @PostMapping
    public ResponseEntity<OpportunityResponse> createOpportunity(@Valid @RequestBody OpportunityRequest request, Authentication authentication) {
        String username = authentication != null ? authentication.getName() : "ANONYMOUS";
        return ResponseEntity.status(HttpStatus.CREATED).body(opportunityService.createOpportunity(request, username));
    }

    @GetMapping
    public ResponseEntity<List<OpportunityResponse>> getAllOpportunities() {
        return ResponseEntity.ok(opportunityService.getAllOpportunities());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OpportunityResponse> getOpportunityById(@PathVariable Long id) {
        return ResponseEntity.ok(opportunityService.getOpportunityById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OpportunityResponse> updateOpportunity(
            @PathVariable Long id,
            @Valid @RequestBody OpportunityRequest request,
            Authentication authentication
    ) {
        String username = authentication != null ? authentication.getName() : "ANONYMOUS";
        return ResponseEntity.ok(opportunityService.updateOpportunity(id, request, username));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOpportunity(@PathVariable Long id, Authentication authentication) {
        String username = authentication != null ? authentication.getName() : "ANONYMOUS";
        opportunityService.deleteOpportunity(id, username);
        return ResponseEntity.ok("Opportunity deleted successfully with ID: " + id);
    }
}
