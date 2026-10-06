package com.jonahjayasingh.CRM.invoice;

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

import com.jonahjayasingh.CRM.invoice.dto.InvoiceRequest;
import com.jonahjayasingh.CRM.invoice.dto.InvoiceResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @PostMapping
    public ResponseEntity<InvoiceResponse> createInvoice(@Valid @RequestBody InvoiceRequest request, Authentication authentication) {
        String username = authentication != null ? authentication.getName() : "ANONYMOUS";
        return ResponseEntity.status(HttpStatus.CREATED).body(invoiceService.createInvoice(request, username));
    }

    @GetMapping
    public ResponseEntity<List<InvoiceResponse>> getAllInvoices() {
        return ResponseEntity.ok(invoiceService.getAllInvoices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponse> getInvoiceById(@PathVariable Long id) {
        return ResponseEntity.ok(invoiceService.getInvoiceById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InvoiceResponse> updateInvoice(
            @PathVariable Long id,
            @Valid @RequestBody InvoiceRequest request,
            Authentication authentication
    ) {
        String username = authentication != null ? authentication.getName() : "ANONYMOUS";
        return ResponseEntity.ok(invoiceService.updateInvoice(id, request, username));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInvoice(@PathVariable Long id, Authentication authentication) {
        String username = authentication != null ? authentication.getName() : "ANONYMOUS";
        invoiceService.deleteInvoice(id, username);
        return ResponseEntity.ok("Invoice deleted successfully with ID: " + id);
    }
}
