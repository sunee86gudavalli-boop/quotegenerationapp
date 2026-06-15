package com.bristolwest.vehicle.dto;

import com.bristolwest.vehicle.entity.Vehicle.UsageType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VehicleRequestDTO {

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    private String vin;

    @NotBlank(message = "Make is required")
    private String make;

    @NotBlank(message = "Model is required")
    private String model;

    @NotNull(message = "Year is required")
    @Min(value = 1900, message = "Year must be valid")
    private Integer year;

    private UsageType usageType = UsageType.PERSONAL;

    private Integer annualMileage;

    private String garagingZip;
}
