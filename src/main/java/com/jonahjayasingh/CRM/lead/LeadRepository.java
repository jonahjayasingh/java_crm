package com.jonahjayasingh.CRM.lead;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeadRepository extends JpaRepository<Lead, Long> {
    List<Lead> findByStatus(LeadStatus status);
    List<Lead> findByAssignedToId(Long userId);
    List<Lead> findByNameContainingIgnoreCase(String name);

    long countByAssignedToId(Long userId);
}
