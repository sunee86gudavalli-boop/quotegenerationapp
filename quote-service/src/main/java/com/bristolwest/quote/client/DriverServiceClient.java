package com.bristolwest.quote.client;

import lombok.Data;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.LocalDateTime;

@FeignClient(name = "driver-service")
public interface DriverServiceClient {

    @PostMapping("/api/drivers")
    DriverResponse createDriver(@RequestBody DriverRequest request);

    @Data
    class DriverRequest {
        private Long customerId;
        private String firstName;
        private String lastName;
        private LocalDate dateOfBirth;
        private String licenseNumber;
        private String licenseState;
        private Integer yearsLicensed;
        private Integer violationsCount;
        private Integer accidentsCount;
    }

    @Data
    class DriverResponse {
        private Long id;
        private Long customerId;
        private String firstName;
        private String lastName;
        private LocalDate dateOfBirth;
        private String licenseNumber;
        private String licenseState;
        private Integer yearsLicensed;
        private Integer violationsCount;
        private Integer accidentsCount;
        private LocalDateTime createdAt;
    }
}
