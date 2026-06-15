package com.bristolwest.driver.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DriverRequestDTO {

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String licenseNumber;
    private String licenseState;

    @Min(value = 0, message = "Years licensed cannot be negative")
    private Integer yearsLicensed;

    @Min(value = 0, message = "Violations count cannot be negative")
    private Integer violationsCount = 0;

    @Min(value = 0, message = "Accidents count cannot be negative")
    private Integer accidentsCount = 0;
}
