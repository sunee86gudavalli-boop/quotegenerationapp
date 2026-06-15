package com.bristolwest.customer;

import com.bristolwest.customer.dto.CustomerRequestDTO;
import com.bristolwest.customer.dto.CustomerResponseDTO;
import com.bristolwest.customer.entity.Customer;
import com.bristolwest.customer.repository.CustomerRepository;
import com.bristolwest.customer.service.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private CustomerRequestDTO request;
    private Customer savedCustomer;

    @BeforeEach
    void setUp() {
        request = new CustomerRequestDTO();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("john.doe@example.com");
        request.setPhone("555-1234");
        request.setDateOfBirth(LocalDate.of(1985, 6, 15));
        request.setAddress("123 Main St");
        request.setCity("Tampa");
        request.setState("FL");
        request.setZipCode("33601");

        savedCustomer = Customer.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("555-1234")
                .dateOfBirth(LocalDate.of(1985, 6, 15))
                .address("123 Main St")
                .city("Tampa")
                .state("FL")
                .zipCode("33601")
                .build();
    }

    @Test
    void createCustomer_success() {
        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        CustomerResponseDTO response = customerService.createCustomer(request);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getFirstName()).isEqualTo("John");
        assertThat(response.getEmail()).isEqualTo("john.doe@example.com");
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void getCustomerById_found() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(savedCustomer));

        CustomerResponseDTO response = customerService.getCustomerById(1L);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getLastName()).isEqualTo("Doe");
    }

    @Test
    void getCustomerById_notFound() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.getCustomerById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Customer not found");
    }

    @Test
    void updateCustomer_success() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(savedCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        CustomerResponseDTO response = customerService.updateCustomer(1L, request);

        assertThat(response.getId()).isEqualTo(1L);
        verify(customerRepository, times(1)).save(any(Customer.class));
    }
}
