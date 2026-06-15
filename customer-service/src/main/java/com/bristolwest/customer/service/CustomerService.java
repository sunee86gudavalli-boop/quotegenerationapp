package com.bristolwest.customer.service;

import com.bristolwest.customer.dto.CustomerRequestDTO;
import com.bristolwest.customer.dto.CustomerResponseDTO;

public interface CustomerService {
    CustomerResponseDTO createCustomer(CustomerRequestDTO request);
    CustomerResponseDTO getCustomerById(Long id);
    CustomerResponseDTO updateCustomer(Long id, CustomerRequestDTO request);
}
