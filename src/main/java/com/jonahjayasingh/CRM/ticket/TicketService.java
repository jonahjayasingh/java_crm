package com.jonahjayasingh.CRM.ticket;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jonahjayasingh.CRM.Customer.Customer;
import com.jonahjayasingh.CRM.Customer.CustomerRepo;
import com.jonahjayasingh.CRM.audit.AuditLogService;
import com.jonahjayasingh.CRM.exception.ResourceNotFoundException;
import com.jonahjayasingh.CRM.ticket.dto.TicketRequest;
import com.jonahjayasingh.CRM.ticket.dto.TicketResponse;
import com.jonahjayasingh.CRM.user.User;
import com.jonahjayasingh.CRM.user.UserRepository;

@Service
@Transactional
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuditLogService auditLogService;

    public TicketResponse createTicket(TicketRequest request, String username) {
        Customer customer = customerRepo.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + request.getCustomerId()));

        Ticket ticket = new Ticket();
        ticket.setSubject(request.getSubject());
        ticket.setDescription(request.getDescription());
        ticket.setStatus(request.getStatus() != null ? request.getStatus() : TicketStatus.OPEN);
        ticket.setPriority(request.getPriority() != null ? request.getPriority() : TicketPriority.MEDIUM);
        ticket.setCustomer(customer);

        if (request.getAssignedToUserId() != null) {
            User user = userRepository.findById(request.getAssignedToUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getAssignedToUserId()));
            ticket.setAssignedTo(user);
        }

        Ticket savedTicket = ticketRepository.save(ticket);
        auditLogService.log("CREATE", "Ticket", savedTicket.getId(), username, "Created support ticket: " + savedTicket.getSubject());
        return mapToResponse(savedTicket);
    }

    public List<TicketResponse> getAllTickets() {
        return ticketRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public TicketResponse getTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + id));
        return mapToResponse(ticket);
    }

    public TicketResponse updateTicket(Long id, TicketRequest request, String username) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + id));

        ticket.setSubject(request.getSubject());
        ticket.setDescription(request.getDescription());
        if (request.getStatus() != null) {
            ticket.setStatus(request.getStatus());
        }
        if (request.getPriority() != null) {
            ticket.setPriority(request.getPriority());
        }

        if (request.getCustomerId() != null) {
            Customer customer = customerRepo.findById(request.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + request.getCustomerId()));
            ticket.setCustomer(customer);
        }

        if (request.getAssignedToUserId() != null) {
            User user = userRepository.findById(request.getAssignedToUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getAssignedToUserId()));
            ticket.setAssignedTo(user);
        }

        Ticket updatedTicket = ticketRepository.save(ticket);
        auditLogService.log("UPDATE", "Ticket", updatedTicket.getId(), username, "Updated ticket: " + updatedTicket.getSubject());
        return mapToResponse(updatedTicket);
    }

    public void deleteTicket(Long id, String username) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + id));
        ticketRepository.delete(ticket);
        auditLogService.log("DELETE", "Ticket", id, username, "Deleted ticket: " + ticket.getSubject());
    }

    private TicketResponse mapToResponse(Ticket ticket) {
        return TicketResponse.builder()
                .id(ticket.getId())
                .subject(ticket.getSubject())
                .description(ticket.getDescription())
                .status(ticket.getStatus())
                .priority(ticket.getPriority())
                .customerId(ticket.getCustomer() != null ? ticket.getCustomer().getId() : null)
                .customerName(ticket.getCustomer() != null ? ticket.getCustomer().getName() : null)
                .assignedToUserId(ticket.getAssignedTo() != null ? ticket.getAssignedTo().getId() : null)
                .assignedToUserName(ticket.getAssignedTo() != null ? ticket.getAssignedTo().getName() : null)
                .createdAt(ticket.getCreatedAt())
                .updatedAt(ticket.getUpdatedAt())
                .build();
    }
}
