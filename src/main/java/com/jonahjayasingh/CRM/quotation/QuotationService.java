package com.jonahjayasingh.CRM.quotation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jonahjayasingh.CRM.Customer.Customer;
import com.jonahjayasingh.CRM.Customer.CustomerRepo;
import com.jonahjayasingh.CRM.audit.AuditLogService;
import com.jonahjayasingh.CRM.exception.ResourceNotFoundException;
import com.jonahjayasingh.CRM.quotation.dto.QuotationRequest;
import com.jonahjayasingh.CRM.quotation.dto.QuotationResponse;

@Service
@Transactional
public class QuotationService {

    @Autowired
    private QuotationRepository quotationRepository;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private AuditLogService auditLogService;

    public QuotationResponse createQuotation(QuotationRequest request, String username) {
        Customer customer = customerRepo.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + request.getCustomerId()));

        Quotation quotation = new Quotation();
        quotation.setQuotationNumber(request.getQuotationNumber());
        quotation.setCustomer(customer);
        quotation.setTotalAmount(request.getTotalAmount());
        quotation.setStatus(request.getStatus() != null ? request.getStatus() : QuotationStatus.DRAFT);
        quotation.setValidUntil(request.getValidUntil());
        quotation.setTermsAndConditions(request.getTermsAndConditions());

        Quotation savedQ = quotationRepository.save(quotation);
        auditLogService.log("CREATE", "Quotation", savedQ.getId(), username, "Created quote: " + savedQ.getQuotationNumber());
        return mapToResponse(savedQ);
    }

    public List<QuotationResponse> getAllQuotations() {
        return quotationRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public QuotationResponse getQuotationById(Long id) {
        Quotation quotation = quotationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quotation not found with id: " + id));
        return mapToResponse(quotation);
    }

    public QuotationResponse updateQuotation(Long id, QuotationRequest request, String username) {
        Quotation quotation = quotationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quotation not found with id: " + id));

        quotation.setQuotationNumber(request.getQuotationNumber());
        quotation.setTotalAmount(request.getTotalAmount());
        if (request.getStatus() != null) {
            quotation.setStatus(request.getStatus());
        }
        quotation.setValidUntil(request.getValidUntil());
        quotation.setTermsAndConditions(request.getTermsAndConditions());

        if (request.getCustomerId() != null) {
            Customer customer = customerRepo.findById(request.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + request.getCustomerId()));
            quotation.setCustomer(customer);
        }

        Quotation updatedQ = quotationRepository.save(quotation);
        auditLogService.log("UPDATE", "Quotation", updatedQ.getId(), username, "Updated quote: " + updatedQ.getQuotationNumber());
        return mapToResponse(updatedQ);
    }

    public void deleteQuotation(Long id, String username) {
        Quotation quotation = quotationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quotation not found with id: " + id));
        quotationRepository.delete(quotation);
        auditLogService.log("DELETE", "Quotation", id, username, "Deleted quote: " + quotation.getQuotationNumber());
    }

    private QuotationResponse mapToResponse(Quotation q) {
        return QuotationResponse.builder()
                .id(q.getId())
                .quotationNumber(q.getQuotationNumber())
                .customerId(q.getCustomer() != null ? q.getCustomer().getId() : null)
                .customerName(q.getCustomer() != null ? q.getCustomer().getName() : null)
                .totalAmount(q.getTotalAmount())
                .status(q.getStatus())
                .validUntil(q.getValidUntil())
                .termsAndConditions(q.getTermsAndConditions())
                .createdAt(q.getCreatedAt())
                .updatedAt(q.getUpdatedAt())
                .build();
    }
}
