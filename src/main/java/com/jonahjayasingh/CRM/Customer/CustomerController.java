package com.jonahjayasingh.CRM.Customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jonahjayasingh.CRM.Customer.dto.CustomerRequest;
import com.jonahjayasingh.CRM.Customer.dto.CustomerResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(
            @Valid @RequestBody CustomerRequest request,
            Authentication authentication
    ) {
        String username = authentication != null ? authentication.getName() : "ANONYMOUS";
        CustomerResponse response = customerService.createCustomer(request, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<CustomerResponse>> searchCustomers(@RequestParam String name) {
        return ResponseEntity.ok(customerService.searchByName(name));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CustomerResponse>> getCustomersByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(customerService.getByUserId(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerRequest request,
            Authentication authentication
    ) {
        String username = authentication != null ? authentication.getName() : "ANONYMOUS";
        return ResponseEntity.ok(customerService.updateCustomer(id, request, username));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long id, Authentication authentication) {
        String username = authentication != null ? authentication.getName() : "ANONYMOUS";
        customerService.deleteById(id, username);
        return ResponseEntity.ok("Customer deleted successfully with ID: " + id);
    }
}
