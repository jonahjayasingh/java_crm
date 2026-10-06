package com.jonahjayasingh.CRM.invoice;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByCustomerId(Long customerId);
    List<Invoice> findByStatus(InvoiceStatus status);

    @Query("SELECT COALESCE(SUM(i.amount), 0) FROM Invoice i WHERE i.status = :status")
    BigDecimal sumAmountByStatus(@Param("status") InvoiceStatus status);
}
