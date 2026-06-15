package com.bristolwest.driver.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class DriverResponseDTO {
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
