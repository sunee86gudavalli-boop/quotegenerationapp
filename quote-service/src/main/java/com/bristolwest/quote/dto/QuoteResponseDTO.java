package com.bristolwest.quote.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class QuoteResponseDTO {
    private Long id;
    private String quoteNumber;
    private Long customerId;
    private Long vehicleId;
    private Long driverId;
    private String coverageType;
    private BigDecimal basePremium;
    private BigDecimal liabilityPremium;
    private BigDecimal collisionPremium;
    private BigDecimal comprehensivePremium;
    private BigDecimal totalPremium;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    // Customer summary
    private String customerName;
    private String customerEmail;
    // Vehicle summary
    private String vehicleMakeModel;
    private Integer vehicleYear;
}
