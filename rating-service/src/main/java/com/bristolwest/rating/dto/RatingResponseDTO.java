package com.bristolwest.rating.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class RatingResponseDTO {
    private BigDecimal basePremium;
    private BigDecimal liabilityPremium;
    private BigDecimal collisionPremium;
    private BigDecimal comprehensivePremium;
    private BigDecimal totalPremium;
    private String coverageType;
}
