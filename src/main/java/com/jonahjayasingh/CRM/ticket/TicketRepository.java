package com.jonahjayasingh.CRM.ticket;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByStatus(TicketStatus status);
    List<Ticket> findByCustomerId(Long customerId);
    List<Ticket> findByAssignedToId(Long userId);

    long countByStatus(TicketStatus status);
    long countByAssignedToIdAndStatusNotIn(Long userId, Collection<TicketStatus> statuses);
}
