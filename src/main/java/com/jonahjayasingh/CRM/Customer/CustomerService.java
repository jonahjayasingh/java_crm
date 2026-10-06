package com.jonahjayasingh.CRM.Customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jonahjayasingh.CRM.Customer.dto.CustomerRequest;
import com.jonahjayasingh.CRM.Customer.dto.CustomerResponse;
import com.jonahjayasingh.CRM.audit.AuditLogService;
import com.jonahjayasingh.CRM.user.User;
import com.jonahjayasingh.CRM.user.UserRepository;

@Service
@Transactional
public class CustomerService {

    @Autowired
    private CustomerRepo repo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuditLogService auditLogService;

    public CustomerResponse createCustomer(CustomerRequest request, String username) {
        User user = userRepository.findByName(username)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setPhoneNo(request.getPhoneNo());
        customer.setCollegeName(request.getCollegeName());
        customer.setIsInterested(request.getIsInterested());
        customer.setNote(request.getNote());
        customer.setUser(user);

        Customer savedCustomer = repo.save(customer);
        auditLogService.log("CREATE", "Customer", savedCustomer.getId(), username, "Created customer: " + savedCustomer.getName());
        return mapToResponse(savedCustomer);
    }

    public List<CustomerResponse> getAllCustomers() {
        return repo.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public CustomerResponse getById(Long id) {
        Customer customer = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        return mapToResponse(customer);
    }

    public List<CustomerResponse> searchByName(String name) {
        return repo.findByNameContainingIgnoreCase(name).stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<CustomerResponse> getByUserId(Long userId) {
        return repo.findByUserId(userId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    public CustomerResponse updateCustomer(Long id, CustomerRequest request, String username) {
        Customer customer = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));

        customer.setName(request.getName());
        customer.setPhoneNo(request.getPhoneNo());
        customer.setCollegeName(request.getCollegeName());
        customer.setIsInterested(request.getIsInterested());
        customer.setNote(request.getNote());

        Customer updatedCustomer = repo.save(customer);
        auditLogService.log("UPDATE", "Customer", updatedCustomer.getId(), username, "Updated customer details for: " + updatedCustomer.getName());
        return mapToResponse(updatedCustomer);
    }

    public void deleteById(Long id, String username) {
        Customer customer = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        repo.delete(customer);
        auditLogService.log("DELETE", "Customer", id, username, "Deleted customer: " + customer.getName());
    }

    private CustomerResponse mapToResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .phoneNo(customer.getPhoneNo())
                .collegeName(customer.getCollegeName())
                .isInterested(customer.getIsInterested())
                .note(customer.getNote())
                .createAt(customer.getCreateAt())
                .updateAt(customer.getUpdateAt())
                .userId(customer.getUser() != null ? customer.getUser().getId() : null)
                .userName(customer.getUser() != null ? customer.getUser().getName() : null)
                .build();
    }
}
