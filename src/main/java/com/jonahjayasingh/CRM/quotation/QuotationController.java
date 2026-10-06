package com.jonahjayasingh.CRM.quotation;

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

import com.jonahjayasingh.CRM.quotation.dto.QuotationRequest;
import com.jonahjayasingh.CRM.quotation.dto.QuotationResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/quotations")
public class QuotationController {

    @Autowired
    private QuotationService quotationService;

    @PostMapping
    public ResponseEntity<QuotationResponse> createQuotation(@Valid @RequestBody QuotationRequest request, Authentication authentication) {
        String username = authentication != null ? authentication.getName() : "ANONYMOUS";
        return ResponseEntity.status(HttpStatus.CREATED).body(quotationService.createQuotation(request, username));
    }

    @GetMapping
    public ResponseEntity<List<QuotationResponse>> getAllQuotations() {
        return ResponseEntity.ok(quotationService.getAllQuotations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuotationResponse> getQuotationById(@PathVariable Long id) {
        return ResponseEntity.ok(quotationService.getQuotationById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuotationResponse> updateQuotation(
            @PathVariable Long id,
            @Valid @RequestBody QuotationRequest request,
            Authentication authentication
    ) {
        String username = authentication != null ? authentication.getName() : "ANONYMOUS";
        return ResponseEntity.ok(quotationService.updateQuotation(id, request, username));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteQuotation(@PathVariable Long id, Authentication authentication) {
        String username = authentication != null ? authentication.getName() : "ANONYMOUS";
        quotationService.deleteQuotation(id, username);
        return ResponseEntity.ok("Quotation deleted successfully with ID: " + id);
    }
}
