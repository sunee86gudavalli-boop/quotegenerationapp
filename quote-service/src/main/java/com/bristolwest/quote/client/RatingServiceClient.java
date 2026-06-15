package com.bristolwest.quote.client;

import lombok.Data;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;

@FeignClient(name = "rating-service")
public interface RatingServiceClient {

    @PostMapping("/api/rating/calculate")
    RatingResponse calculatePremium(@RequestBody RatingRequest request);

    @Data
    class RatingRequest {
        private Integer driverAge;
        private Integer violations;
        private Integer accidents;
        private Integer vehicleYear;
        private Integer annualMileage;
        private String coverageType;
        private String zipCode;
    }

    @Data
    class RatingResponse {
        private BigDecimal basePremium;
        private BigDecimal liabilityPremium;
        private BigDecimal collisionPremium;
        private BigDecimal comprehensivePremium;
        private BigDecimal totalPremium;
        private String coverageType;
    }
}
