package com.jonahjayasingh.CRM.invoice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jonahjayasingh.CRM.Customer.Customer;
import com.jonahjayasingh.CRM.Customer.CustomerRepo;
import com.jonahjayasingh.CRM.audit.AuditLogService;
import com.jonahjayasingh.CRM.exception.ResourceNotFoundException;
import com.jonahjayasingh.CRM.invoice.dto.InvoiceRequest;
import com.jonahjayasingh.CRM.invoice.dto.InvoiceResponse;

@Service
@Transactional
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private AuditLogService auditLogService;

    public InvoiceResponse createInvoice(InvoiceRequest request, String username) {
        Customer customer = customerRepo.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + request.getCustomerId()));

        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(request.getInvoiceNumber());
        invoice.setCustomer(customer);
        invoice.setAmount(request.getAmount());
        invoice.setStatus(request.getStatus() != null ? request.getStatus() : InvoiceStatus.UNPAID);
        invoice.setIssueDate(request.getIssueDate());
        invoice.setDueDate(request.getDueDate());

        Invoice savedInv = invoiceRepository.save(invoice);
        auditLogService.log("CREATE", "Invoice", savedInv.getId(), username, "Issued invoice: " + savedInv.getInvoiceNumber() + " ($" + savedInv.getAmount() + ")");
        return mapToResponse(savedInv);
    }

    public List<InvoiceResponse> getAllInvoices() {
        return invoiceRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public InvoiceResponse getInvoiceById(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id));
        return mapToResponse(invoice);
    }

    public InvoiceResponse updateInvoice(Long id, InvoiceRequest request, String username) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id));

        invoice.setInvoiceNumber(request.getInvoiceNumber());
        invoice.setAmount(request.getAmount());
        if (request.getStatus() != null) {
            invoice.setStatus(request.getStatus());
        }
        invoice.setIssueDate(request.getIssueDate());
        invoice.setDueDate(request.getDueDate());

        if (request.getCustomerId() != null) {
            Customer customer = customerRepo.findById(request.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + request.getCustomerId()));
            invoice.setCustomer(customer);
        }

        Invoice updatedInv = invoiceRepository.save(invoice);
        auditLogService.log("UPDATE", "Invoice", updatedInv.getId(), username, "Updated invoice: " + updatedInv.getInvoiceNumber());
        return mapToResponse(updatedInv);
    }

    public void deleteInvoice(Long id, String username) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id));
        invoiceRepository.delete(invoice);
        auditLogService.log("DELETE", "Invoice", id, username, "Deleted invoice: " + invoice.getInvoiceNumber());
    }

    private InvoiceResponse mapToResponse(Invoice inv) {
        return InvoiceResponse.builder()
                .id(inv.getId())
                .invoiceNumber(inv.getInvoiceNumber())
                .customerId(inv.getCustomer() != null ? inv.getCustomer().getId() : null)
                .customerName(inv.getCustomer() != null ? inv.getCustomer().getName() : null)
                .amount(inv.getAmount())
                .status(inv.getStatus())
                .issueDate(inv.getIssueDate())
                .dueDate(inv.getDueDate())
                .createdAt(inv.getCreatedAt())
                .updatedAt(inv.getUpdatedAt())
                .build();
    }
}
