package com.jonahjayasingh.CRM.quotation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuotationRepository extends JpaRepository<Quotation, Long> {
    List<Quotation> findByCustomerId(Long customerId);
    List<Quotation> findByStatus(QuotationStatus status);
}
