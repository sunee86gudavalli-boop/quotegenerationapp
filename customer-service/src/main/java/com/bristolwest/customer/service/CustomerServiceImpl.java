package com.bristolwest.customer.service;

import com.bristolwest.customer.dto.CustomerRequestDTO;
import com.bristolwest.customer.dto.CustomerResponseDTO;
import com.bristolwest.customer.entity.Customer;
import com.bristolwest.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public CustomerResponseDTO createCustomer(CustomerRequestDTO request) {
        Customer customer = Customer.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .dateOfBirth(request.getDateOfBirth())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .zipCode(request.getZipCode())
                .build();

        Customer saved = customerRepository.save(customer);
        return toResponse(saved);
    }

    @Override
    public CustomerResponseDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        return toResponse(customer);
    }

    @Override
    public CustomerResponseDTO updateCustomer(Long id, CustomerRequestDTO request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));

        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setDateOfBirth(request.getDateOfBirth());
        customer.setAddress(request.getAddress());
        customer.setCity(request.getCity());
        customer.setState(request.getState());
        customer.setZipCode(request.getZipCode());

        Customer updated = customerRepository.save(customer);
        return toResponse(updated);
    }

    private CustomerResponseDTO toResponse(Customer c) {
        return CustomerResponseDTO.builder()
                .id(c.getId())
                .firstName(c.getFirstName())
                .lastName(c.getLastName())
                .email(c.getEmail())
                .phone(c.getPhone())
                .dateOfBirth(c.getDateOfBirth())
                .address(c.getAddress())
                .city(c.getCity())
                .state(c.getState())
                .zipCode(c.getZipCode())
                .createdAt(c.getCreatedAt())
                .build();
    }
}
