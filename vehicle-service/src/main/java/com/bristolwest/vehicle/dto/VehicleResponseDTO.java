package com.bristolwest.vehicle.dto;

import com.bristolwest.vehicle.entity.Vehicle.UsageType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class VehicleResponseDTO {
    private Long id;
    private Long customerId;
    private String vin;
    private String make;
    private String model;
    private Integer year;
    private UsageType usageType;
    private Integer annualMileage;
    private String garagingZip;
    private LocalDateTime createdAt;
}
