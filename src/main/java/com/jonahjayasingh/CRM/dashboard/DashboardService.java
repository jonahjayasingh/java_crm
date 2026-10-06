package com.jonahjayasingh.CRM.dashboard;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jonahjayasingh.CRM.Customer.CustomerRepo;
import com.jonahjayasingh.CRM.invoice.Invoice;
import com.jonahjayasingh.CRM.invoice.InvoiceRepository;
import com.jonahjayasingh.CRM.invoice.InvoiceStatus;
import com.jonahjayasingh.CRM.lead.LeadRepository;
import com.jonahjayasingh.CRM.opportunity.Opportunity;
import com.jonahjayasingh.CRM.opportunity.OpportunityRepository;
import com.jonahjayasingh.CRM.product.ProductRepository;
import com.jonahjayasingh.CRM.quotation.QuotationRepository;
import com.jonahjayasingh.CRM.task.TaskPriority;

import com.jonahjayasingh.CRM.task.TaskRepository;
import com.jonahjayasingh.CRM.task.TaskStatus;
import com.jonahjayasingh.CRM.ticket.TicketRepository;
import com.jonahjayasingh.CRM.ticket.TicketStatus;
import com.jonahjayasingh.CRM.user.User;
import com.jonahjayasingh.CRM.user.UserRepository;

@Service
public class DashboardService {

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private LeadRepository leadRepository;

    @Autowired
    private OpportunityRepository opportunityRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private QuotationRepository quotationRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private com.jonahjayasingh.CRM.event.EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    public DashboardResponse getDashboardMetrics(String username) {
        Optional<User> userOpt = userRepository.findByName(username);
        String role = userOpt.map(u -> u.getRole().name()).orElse("EMPLOYEE");
        Long userId = userOpt.map(User::getId).orElse(null);

        long totalCustomers = customerRepo.count();
        long totalLeads = leadRepository.count();
        long totalOpportunities = opportunityRepository.count();
        long totalTasks = taskRepository.count();
        long openTickets = ticketRepository.countByStatus(TicketStatus.OPEN);
        long totalProducts = productRepository.count();
        long totalQuotations = quotationRepository.count();
        long totalInvoices = invoiceRepository.count();
        long totalEvents = eventRepository.count();

        BigDecimal totalPipelineValue = opportunityRepository.sumTotalAmount();
        BigDecimal totalRevenueCollected = invoiceRepository.sumAmountByStatus(InvoiceStatus.PAID);

        long myAssignedLeads = 0;
        long myPendingTasks = 0;
        long myOpenTickets = 0;

        if (userId != null) {
            myAssignedLeads = leadRepository.countByAssignedToId(userId);
            myPendingTasks = taskRepository.countByAssignedToIdAndStatusNot(userId, TaskStatus.COMPLETED);
            myOpenTickets = ticketRepository.countByAssignedToIdAndStatusNotIn(userId, List.of(TicketStatus.CLOSED, TicketStatus.RESOLVED));
        }

        return DashboardResponse.builder()
                .role(role)
                .totalCustomers(totalCustomers)
                .totalLeads(totalLeads)
                .totalOpportunities(totalOpportunities)
                .totalTasks(totalTasks)
                .openTickets(openTickets)
                .totalProducts(totalProducts)
                .totalQuotations(totalQuotations)
                .totalInvoices(totalInvoices)
                .totalEvents(totalEvents)
                .totalPipelineValue(totalPipelineValue)
                .totalRevenueCollected(totalRevenueCollected)
                .myAssignedLeads(myAssignedLeads)
                .myPendingTasks(myPendingTasks)
                .myOpenTickets(myOpenTickets)
                .build();
    }
}
