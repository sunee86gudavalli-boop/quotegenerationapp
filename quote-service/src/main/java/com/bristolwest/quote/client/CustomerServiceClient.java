package com.bristolwest.quote.client;

import lombok.Data;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.LocalDateTime;

@FeignClient(name = "customer-service")
public interface CustomerServiceClient {

    @PostMapping("/api/customers")
    CustomerResponse createCustomer(@RequestBody CustomerRequest request);

    @Data
    class CustomerRequest {
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private LocalDate dateOfBirth;
        private String address;
        private String city;
        private String state;
        private String zipCode;
    }

    @Data
    class CustomerResponse {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private LocalDate dateOfBirth;
        private String address;
        private String city;
        private String state;
        private String zipCode;
        private LocalDateTime createdAt;
    }
}
