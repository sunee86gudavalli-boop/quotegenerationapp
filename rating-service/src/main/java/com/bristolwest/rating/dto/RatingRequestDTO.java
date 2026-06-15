package com.bristolwest.rating.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RatingRequestDTO {

    @NotNull(message = "Driver age is required")
    @Min(value = 16, message = "Driver must be at least 16")
    private Integer driverAge;

    @NotNull(message = "Violations count is required")
    @Min(value = 0)
    private Integer violations;

    @NotNull(message = "Accidents count is required")
    @Min(value = 0)
    private Integer accidents;

    @NotNull(message = "Vehicle year is required")
    private Integer vehicleYear;

    @NotNull(message = "Annual mileage is required")
    @Min(value = 0)
    private Integer annualMileage;

    @NotBlank(message = "Coverage type is required")
    private String coverageType;  // LIABILITY, STANDARD, FULL

    private String zipCode;
}
