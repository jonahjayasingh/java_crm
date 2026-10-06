package com.jonahjayasingh.CRM.opportunity;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OpportunityRepository extends JpaRepository<Opportunity, Long> {
    List<Opportunity> findByStage(OpportunityStage stage);
    List<Opportunity> findByCustomerId(Long customerId);

    @Query("SELECT COALESCE(SUM(o.amount), 0) FROM Opportunity o")
    BigDecimal sumTotalAmount();
}
