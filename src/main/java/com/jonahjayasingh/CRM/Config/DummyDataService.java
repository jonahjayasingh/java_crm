package com.jonahjayasingh.CRM.Config;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jonahjayasingh.CRM.Customer.Customer;
import com.jonahjayasingh.CRM.Customer.CustomerRepo;
import com.jonahjayasingh.CRM.audit.AuditLog;
import com.jonahjayasingh.CRM.audit.AuditLogRepository;
import com.jonahjayasingh.CRM.event.Event;
import com.jonahjayasingh.CRM.event.EventRepository;
import com.jonahjayasingh.CRM.invoice.Invoice;
import com.jonahjayasingh.CRM.invoice.InvoiceRepository;
import com.jonahjayasingh.CRM.invoice.InvoiceStatus;
import com.jonahjayasingh.CRM.lead.Lead;
import com.jonahjayasingh.CRM.lead.LeadRepository;
import com.jonahjayasingh.CRM.lead.LeadStatus;
import com.jonahjayasingh.CRM.opportunity.Opportunity;
import com.jonahjayasingh.CRM.opportunity.OpportunityRepository;
import com.jonahjayasingh.CRM.opportunity.OpportunityStage;
import com.jonahjayasingh.CRM.product.Product;
import com.jonahjayasingh.CRM.product.ProductRepository;
import com.jonahjayasingh.CRM.quotation.Quotation;
import com.jonahjayasingh.CRM.quotation.QuotationRepository;
import com.jonahjayasingh.CRM.quotation.QuotationStatus;
import com.jonahjayasingh.CRM.task.Task;
import com.jonahjayasingh.CRM.task.TaskPriority;
import com.jonahjayasingh.CRM.task.TaskRepository;
import com.jonahjayasingh.CRM.task.TaskStatus;
import com.jonahjayasingh.CRM.ticket.Ticket;
import com.jonahjayasingh.CRM.ticket.TicketPriority;
import com.jonahjayasingh.CRM.ticket.TicketRepository;
import com.jonahjayasingh.CRM.ticket.TicketStatus;
import com.jonahjayasingh.CRM.user.Role;
import com.jonahjayasingh.CRM.user.User;
import com.jonahjayasingh.CRM.user.UserRepository;

@Service
public class DummyDataService {

        private final UserRepository userRepository;
        private final CustomerRepo customerRepo;
        private final LeadRepository leadRepository;
        private final OpportunityRepository opportunityRepository;
        private final TaskRepository taskRepository;
        private final EventRepository eventRepository;
        private final TicketRepository ticketRepository;
        private final ProductRepository productRepository;
        private final QuotationRepository quotationRepository;
        private final InvoiceRepository invoiceRepository;
        private final AuditLogRepository auditLogRepository;
        private final PasswordEncoder passwordEncoder;
        private final JdbcTemplate jdbcTemplate;

        public DummyDataService(
                        UserRepository userRepository,
                        CustomerRepo customerRepo,
                        LeadRepository leadRepository,
                        OpportunityRepository opportunityRepository,
                        TaskRepository taskRepository,
                        EventRepository eventRepository,
                        TicketRepository ticketRepository,
                        ProductRepository productRepository,
                        QuotationRepository quotationRepository,
                        InvoiceRepository invoiceRepository,
                        AuditLogRepository auditLogRepository,
                        PasswordEncoder passwordEncoder,
                        JdbcTemplate jdbcTemplate) {
                this.userRepository = userRepository;
                this.customerRepo = customerRepo;
                this.leadRepository = leadRepository;
                this.opportunityRepository = opportunityRepository;
                this.taskRepository = taskRepository;
                this.eventRepository = eventRepository;
                this.ticketRepository = ticketRepository;
                this.productRepository = productRepository;
                this.quotationRepository = quotationRepository;
                this.invoiceRepository = invoiceRepository;
                this.auditLogRepository = auditLogRepository;
                this.passwordEncoder = passwordEncoder;
                this.jdbcTemplate = jdbcTemplate;
        }

        private void ensurePostgresSequences() {
                try {
                        jdbcTemplate.execute(
                                        "DO $$\n" +
                                                        "DECLARE\n" +
                                                        "    rec RECORD;\n" +
                                                        "BEGIN\n" +
                                                        "    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'customer') THEN\n"
                                                        +
                                                        "        IF NOT EXISTS (\n" +
                                                        "            SELECT 1 FROM information_schema.columns \n" +
                                                        "            WHERE table_name = 'customer' AND column_name = 'id' AND is_identity = 'YES'\n"
                                                        +
                                                        "        ) THEN\n" +
                                                        "            ALTER TABLE customer ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY;\n"
                                                        +
                                                        "        END IF;\n" +
                                                        "        FOR rec IN \n" +
                                                        "            SELECT column_name \n" +
                                                        "            FROM information_schema.columns \n" +
                                                        "            WHERE table_name = 'customer' AND is_nullable = 'NO' AND column_name NOT IN ('id', 'name', 'phone_no', 'note', 'user_id', 'is_interested', 'create_at', 'update_at')\n"
                                                        +
                                                        "        LOOP\n" +
                                                        "            EXECUTE 'ALTER TABLE customer ALTER COLUMN \"' || rec.column_name || '\" DROP NOT NULL;';\n"
                                                        +
                                                        "        END LOOP;\n" +
                                                        "    END IF;\n" +
                                                        "EXCEPTION WHEN OTHERS THEN NULL;\n" +
                                                        "END $$;");
                } catch (Exception e) {
                        // Non-PostgreSQL or already modified
                }
        }

        @Transactional
        public String seedAllDummyData(boolean force) {
                ensurePostgresSequences();
                StringBuilder result = new StringBuilder("Dummy Data Seeding Summary:\n");

                // 1. Users
                User admin = userRepository.findByName("admin").orElseGet(() -> {
                        User u = new User();
                        u.setName("admin");
                        u.setEmail("admin@apexcrm.com");
                        u.setPassword(passwordEncoder.encode("password123"));
                        u.setRole(Role.ADMIN);
                        u.setEnabled(true);
                        return userRepository.save(u);
                });

                User manager = userRepository.findByName("manager").orElseGet(() -> {
                        User u = new User();
                        u.setName("manager");
                        u.setEmail("manager@apexcrm.com");
                        u.setPassword(passwordEncoder.encode("password123"));
                        u.setRole(Role.MANAGER);
                        u.setEnabled(true);
                        return userRepository.save(u);
                });

                User sales = userRepository.findByName("sales1").orElseGet(() -> {
                        User u = new User();
                        u.setName("sales1");
                        u.setEmail("sales1@apexcrm.com");
                        u.setPassword(passwordEncoder.encode("password123"));
                        u.setRole(Role.SALES);
                        u.setEnabled(true);
                        return userRepository.save(u);
                });

                User employee = userRepository.findByName("employee1").orElseGet(() -> {
                        User u = new User();
                        u.setName("employee1");
                        u.setEmail("employee1@apexcrm.com");
                        u.setPassword(passwordEncoder.encode("password123"));
                        u.setRole(Role.EMPLOYEE);
                        u.setEnabled(true);
                        return userRepository.save(u);
                });

                result.append("- Users: Verified admin, manager, sales1, employee1\n");

                // 2. Customers
                Customer c1 = getOrCreateCustomer("Acme Enterprise Solutions", "9876543210", "Harvard Business School",
                                true, "Key enterprise account interested in full SaaS suite migration.", admin);
                Customer c2 = getOrCreateCustomer("Apex Global Tech", "9123456789", "MIT Innovation Lab", true,
                                "Contract renewal pending for Q4 expansion.", sales);
                Customer c3 = getOrCreateCustomer("Nexus Health Systems", "9887766554", "Stanford Medical Center", true,
                                "Requires HIPAA compliance auditing and custom SLA.", manager);
                Customer c4 = getOrCreateCustomer("Starlight Retail Ltd", "9776655443", "Oxford Research Guild", false,
                                "Expressed interest in CRM Automation module.", sales);
                getOrCreateCustomer("CyberShield Security", "9665544332", "Cambridge Tech", true,
                                "Evaluating Quotation Q-2026-001.", admin);

                result.append("- Customers: Verified 5 records\n");

                // 3. Leads
                if (leadRepository.count() == 0) {
                        Lead l1 = Lead.builder()
                                        .name("Sarah Jenkins")
                                        .email("sarah.j@acme.com")
                                        .phone("9876543210")
                                        .company("Acme Corp")
                                        .source("Website Form")
                                        .status(LeadStatus.QUALIFIED)
                                        .notes("Inquired about 50 enterprise user licenses.")
                                        .assignedTo(sales)
                                        .build();

                        Lead l2 = Lead.builder()
                                        .name("Michael Chang")
                                        .email("m.chang@apextech.io")
                                        .phone("9123456789")
                                        .company("Apex Tech")
                                        .source("LinkedIn Campaign")
                                        .status(LeadStatus.CONTACTED)
                                        .notes("Scheduled a live product demo.")
                                        .assignedTo(sales)
                                        .build();

                        Lead l3 = Lead.builder()
                                        .name("Emily Davis")
                                        .email("emily@nexushealth.org")
                                        .phone("9887766554")
                                        .company("Nexus Health")
                                        .source("Referral")
                                        .status(LeadStatus.NEW)
                                        .notes("Inquired about API integration gateway.")
                                        .assignedTo(manager)
                                        .build();

                        Lead l4 = Lead.builder()
                                        .name("Robert Vance")
                                        .email("vance@starlight.co")
                                        .phone("9776655443")
                                        .company("Starlight Retail")
                                        .source("Cold Outreach")
                                        .status(LeadStatus.CONVERTED)
                                        .notes("Converted to active customer account.")
                                        .assignedTo(sales)
                                        .build();

                        leadRepository.saveAll(List.of(l1, l2, l3, l4));
                        result.append("- Leads: Seeded 4 records\n");
                } else {
                        result.append("- Leads: Already present (" + leadRepository.count() + " records)\n");
                }

                // 4. Opportunities
                if (opportunityRepository.count() == 0 && c1 != null) {
                        Opportunity o1 = Opportunity.builder()
                                        .title("Acme Corp - Enterprise Cloud Migration")
                                        .amount(new BigDecimal("120000.00"))
                                        .stage(OpportunityStage.PROPOSAL)
                                        .probability(75)
                                        .expectedCloseDate(LocalDate.now().plusDays(30))
                                        .customer(c1)
                                        .build();

                        Opportunity o2 = Opportunity.builder()
                                        .title("Apex Tech - SaaS Expansion Deal")
                                        .amount(new BigDecimal("85000.00"))
                                        .stage(OpportunityStage.NEGOTIATION)
                                        .probability(85)
                                        .expectedCloseDate(LocalDate.now().plusDays(15))
                                        .customer(c2 != null ? c2 : c1)
                                        .build();

                        Opportunity o3 = Opportunity.builder()
                                        .title("Nexus Health - Compliance Suite")
                                        .amount(new BigDecimal("250000.00"))
                                        .stage(OpportunityStage.QUALIFICATION)
                                        .probability(50)
                                        .expectedCloseDate(LocalDate.now().plusDays(60))
                                        .customer(c3 != null ? c3 : c1)
                                        .build();

                        Opportunity o4 = Opportunity.builder()
                                        .title("Starlight Retail - POS & CRM Integration")
                                        .amount(new BigDecimal("45000.00"))
                                        .stage(OpportunityStage.CLOSED_WON)
                                        .probability(100)
                                        .expectedCloseDate(LocalDate.now().minusDays(5))
                                        .customer(c4 != null ? c4 : c1)
                                        .build();

                        opportunityRepository.saveAll(List.of(o1, o2, o3, o4));
                        result.append("- Opportunities: Seeded 4 records\n");
                } else {
                        result.append("- Opportunities: Already present (" + opportunityRepository.count()
                                        + " records)\n");
                }

                // 5. Tasks (Kanban Board)
                if (taskRepository.count() == 0) {
                        Task t1 = Task.builder()
                                        .title("Prepare Q4 Sales Pitch Deck")
                                        .description("Draft updated presentation slides for enterprise clients")
                                        .status(TaskStatus.PENDING)
                                        .priority(TaskPriority.HIGH)
                                        .dueDate(LocalDate.now().plusDays(5))
                                        .assignedTo(sales)
                                        .build();

                        Task t2 = Task.builder()
                                        .title("Setup Customer Onboarding Portal")
                                        .description("Configure initial workspace and user roles for Apex Tech")
                                        .status(TaskStatus.IN_PROGRESS)
                                        .priority(TaskPriority.HIGH)
                                        .dueDate(LocalDate.now().plusDays(2))
                                        .assignedTo(manager)
                                        .build();

                        Task t3 = Task.builder()
                                        .title("Security Audit & Log Review")
                                        .description("Perform periodic audit log inspection and compliance verification")
                                        .status(TaskStatus.COMPLETED)
                                        .priority(TaskPriority.MEDIUM)
                                        .dueDate(LocalDate.now().minusDays(1))
                                        .assignedTo(admin)
                                        .build();

                        Task t4 = Task.builder()
                                        .title("Follow up with Nexus Health on API Specs")
                                        .description("Send REST API docs and authentication details")
                                        .status(TaskStatus.PENDING)
                                        .priority(TaskPriority.MEDIUM)
                                        .dueDate(LocalDate.now().plusDays(3))
                                        .assignedTo(sales)
                                        .build();

                        taskRepository.saveAll(List.of(t1, t2, t3, t4));
                        result.append("- Tasks: Seeded 4 records\n");
                } else {
                        result.append("- Tasks: Already present (" + taskRepository.count() + " records)\n");
                }

                // 6. Events (Calendar Grid)
                if (eventRepository.count() == 0) {
                        LocalDateTime now = LocalDateTime.now();

                        Event ev1 = new Event();
                        ev1.setTitle("Acme Corp Q4 Strategy Meeting");
                        ev1.setDescription("Discuss enterprise cloud suite rollout & milestone timeline");
                        ev1.setStartTime(now.withHour(10).withMinute(0));
                        ev1.setEndTime(now.withHour(11).withMinute(30));
                        ev1.setLocation("Zoom Meeting");
                        ev1.setEventType("MEETING");
                        ev1.setStatus("SCHEDULED");
                        if (c1 != null) {
                                ev1.setCustomerId(c1.getId());
                                ev1.setCustomerName(c1.getName());
                        }
                        ev1.setAssignedToUserId(sales.getId());
                        ev1.setAssignedToUserName(sales.getName());

                        Event ev2 = new Event();
                        ev2.setTitle("Apex Tech Live Product Demo");
                        ev2.setDescription("Product walkthrough with engineering lead");
                        ev2.setStartTime(now.plusDays(2).withHour(14).withMinute(0));
                        ev2.setEndTime(now.plusDays(2).withHour(15).withMinute(0));
                        ev2.setLocation("Google Meet");
                        ev2.setEventType("DEMO");
                        ev2.setStatus("SCHEDULED");
                        if (c2 != null) {
                                ev2.setCustomerId(c2.getId());
                                ev2.setCustomerName(c2.getName());
                        }
                        ev2.setAssignedToUserId(sales.getId());
                        ev2.setAssignedToUserName(sales.getName());

                        Event ev3 = new Event();
                        ev3.setTitle("Nexus Health Technical Follow-up");
                        ev3.setDescription("Address security compliance questions");
                        ev3.setStartTime(now.plusDays(5).withHour(11).withMinute(0));
                        ev3.setEndTime(now.plusDays(5).withHour(11).withMinute(45));
                        ev3.setLocation("Conference Room B");
                        ev3.setEventType("FOLLOW_UP");
                        ev3.setStatus("SCHEDULED");
                        if (c3 != null) {
                                ev3.setCustomerId(c3.getId());
                                ev3.setCustomerName(c3.getName());
                        }
                        ev3.setAssignedToUserId(manager.getId());
                        ev3.setAssignedToUserName(manager.getName());

                        Event ev4 = new Event();
                        ev4.setTitle("Enterprise CRM Architecture Webinar");
                        ev4.setDescription("Public webinar on modern CRM workflows");
                        ev4.setStartTime(now.plusDays(8).withHour(16).withMinute(0));
                        ev4.setEndTime(now.plusDays(8).withHour(17).withMinute(30));
                        ev4.setLocation("YouTube Live");
                        ev4.setEventType("WEBINAR");
                        ev4.setStatus("SCHEDULED");
                        ev4.setAssignedToUserId(admin.getId());
                        ev4.setAssignedToUserName(admin.getName());

                        eventRepository.saveAll(List.of(ev1, ev2, ev3, ev4));
                        result.append("- Events: Seeded 4 records\n");
                } else {
                        result.append("- Events: Already present (" + eventRepository.count() + " records)\n");
                }

                // 7. Tickets
                if (ticketRepository.count() == 0 && c1 != null) {
                        Ticket tk1 = Ticket.builder()
                                        .subject("SSO Authentication Timeout on Login")
                                        .description("Users reporting session expiration after 15 minutes of inactivity.")
                                        .status(TicketStatus.OPEN)
                                        .priority(TicketPriority.HIGH)
                                        .customer(c1)
                                        .assignedTo(employee)
                                        .build();

                        Ticket tk2 = Ticket.builder()
                                        .subject("Invoice PDF Export Formatting Issue")
                                        .description("Logo is slightly misaligned on downloaded PDF invoice.")
                                        .status(TicketStatus.IN_PROGRESS)
                                        .priority(TicketPriority.MEDIUM)
                                        .customer(c2 != null ? c2 : c1)
                                        .assignedTo(employee)
                                        .build();

                        Ticket tk3 = Ticket.builder()
                                        .subject("Custom Webhook Notification Request")
                                        .description("Client requested real-time webhook triggers for ticket status changes.")
                                        .status(TicketStatus.RESOLVED)
                                        .priority(TicketPriority.LOW)
                                        .customer(c3 != null ? c3 : c1)
                                        .assignedTo(admin)
                                        .build();

                        ticketRepository.saveAll(List.of(tk1, tk2, tk3));
                        result.append("- Tickets: Seeded 3 records\n");
                } else {
                        result.append("- Tickets: Already present (" + ticketRepository.count() + " records)\n");
                }

                // 8. Products
                getOrCreateProduct("Apex CRM Enterprise Cloud",
                                "Full enterprise SaaS CRM suite with unlimited data analytics & audit logging",
                                new BigDecimal("1200.00"), 500, "Software");
                getOrCreateProduct("APEX API Integration Gateway", "High-throughput REST & GraphQL integration module",
                                new BigDecimal("3500.00"), 100, "Infrastructure");
                getOrCreateProduct("24/7 Dedicated Support Package",
                                "Round-the-clock priority SLA support and dedicated account manager",
                                new BigDecimal("2400.00"), 250, "Support");
                result.append("- Products: Verified 3 records\n");

                // 9. Quotations
                if (quotationRepository.count() == 0 && c1 != null) {
                        Quotation q1 = Quotation.builder()
                                        .quotationNumber("Q-2026-001")
                                        .customer(c1)
                                        .totalAmount(new BigDecimal("120000.00"))
                                        .status(QuotationStatus.SENT)
                                        .validUntil(LocalDate.now().plusDays(30))
                                        .termsAndConditions("Payment net 30 upon license activation.")
                                        .build();

                        Quotation q2 = Quotation.builder()
                                        .quotationNumber("Q-2026-002")
                                        .customer(c2 != null ? c2 : c1)
                                        .totalAmount(new BigDecimal("85000.00"))
                                        .status(QuotationStatus.ACCEPTED)
                                        .validUntil(LocalDate.now().plusDays(15))
                                        .termsAndConditions("Annual billing with 10% prepayment discount.")
                                        .build();

                        quotationRepository.saveAll(List.of(q1, q2));
                        result.append("- Quotations: Seeded 2 records\n");
                } else {
                        result.append("- Quotations: Already present (" + quotationRepository.count() + " records)\n");
                }

                // 10. Invoices
                if (invoiceRepository.count() == 0 && c1 != null) {
                        Invoice inv1 = Invoice.builder()
                                        .invoiceNumber("INV-2026-101")
                                        .customer(c2 != null ? c2 : c1)
                                        .amount(new BigDecimal("85000.00"))
                                        .status(InvoiceStatus.PAID)
                                        .issueDate(LocalDate.now().minusDays(15))
                                        .dueDate(LocalDate.now().plusDays(15))
                                        .build();

                        Invoice inv2 = Invoice.builder()
                                        .invoiceNumber("INV-2026-102")
                                        .customer(c4 != null ? c4 : c1)
                                        .amount(new BigDecimal("45000.00"))
                                        .status(InvoiceStatus.UNPAID)
                                        .issueDate(LocalDate.now().minusDays(5))
                                        .dueDate(LocalDate.now().plusDays(25))
                                        .build();

                        invoiceRepository.saveAll(List.of(inv1, inv2));
                        result.append("- Invoices: Seeded 2 records\n");
                } else {
                        result.append("- Invoices: Already present (" + invoiceRepository.count() + " records)\n");
                }

                // 11. Audit Logs
                if (auditLogRepository.count() == 0) {
                        AuditLog al1 = AuditLog.builder()
                                        .action("CREATE")
                                        .entityName("Customer")
                                        .entityId(c1 != null ? c1.getId() : 1L)
                                        .performedBy("admin")
                                        .details("Created customer Acme Enterprise Solutions")
                                        .timestamp(LocalDateTime.now().minusDays(2))
                                        .build();

                        AuditLog al2 = AuditLog.builder()
                                        .action("UPDATE")
                                        .entityName("Opportunity")
                                        .entityId(1L)
                                        .performedBy("sales1")
                                        .details("Updated stage to PROPOSAL")
                                        .timestamp(LocalDateTime.now().minusDays(1))
                                        .build();

                        auditLogRepository.saveAll(List.of(al1, al2));
                        result.append("- Audit Logs: Seeded 2 records\n");
                } else {
                        result.append("- Audit Logs: Already present (" + auditLogRepository.count() + " records)\n");
                }

                return result.toString();
        }

        private Customer getOrCreateCustomer(String name, String phone, String college, boolean interested, String note,
                        User user) {
                return customerRepo.findAll().stream()
                                .filter(c -> name.equalsIgnoreCase(c.getName()))
                                .findFirst()
                                .orElseGet(() -> {
                                        Customer c = new Customer();
                                        c.setName(name);
                                        c.setPhoneNo(phone);
                                        c.setCollegeName(college);
                                        c.setIsInterested(interested);
                                        c.setNote(note);
                                        c.setUser(user);
                                        c.setCreateAt(LocalDate.now());
                                        c.setUpdateAt(LocalDate.now());
                                        return customerRepo.save(c);
                                });
        }

        private Product getOrCreateProduct(String name, String desc, BigDecimal price, int stock, String category) {
                return productRepository.findByName(name).orElseGet(() -> {
                        Product p = Product.builder()
                                        .name(name)
                                        .description(desc)
                                        .price(price)
                                        .stockQuantity(stock)
                                        .category(category)
                                        .build();
                        return productRepository.save(p);
                });
        }
}
