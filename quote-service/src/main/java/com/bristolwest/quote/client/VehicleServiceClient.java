package com.bristolwest.quote.client;

import lombok.Data;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;

@FeignClient(name = "vehicle-service")
public interface VehicleServiceClient {

    @PostMapping("/api/vehicles")
    VehicleResponse createVehicle(@RequestBody VehicleRequest request);

    @Data
    class VehicleRequest {
        private Long customerId;
        private String vin;
        private String make;
        private String model;
        private Integer year;
        private String usageType;
        private Integer annualMileage;
        private String garagingZip;
    }

    @Data
    class VehicleResponse {
        private Long id;
        private Long customerId;
        private String vin;
        private String make;
        private String model;
        private Integer year;
        private String usageType;
        private Integer annualMileage;
        private String garagingZip;
        private LocalDateTime createdAt;
    }
}
