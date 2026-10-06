package com.jonahjayasingh.CRM.Customer;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Long> {

    List<Customer> findByNameContainingIgnoreCase(String name);

    List<Customer> findByUserId(Long userId);

    List<Customer> findByIsInterested(Boolean isInterested);

}
