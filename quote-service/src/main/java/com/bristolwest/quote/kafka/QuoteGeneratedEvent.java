package com.bristolwest.quote.kafka;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class QuoteGeneratedEvent {
    private Long quoteId;
    private String quoteNumber;
    private String customerEmail;
    private String customerName;
    private BigDecimal totalPremium;
    private String coverageType;
    private String vehicleMakeModel;
    private LocalDateTime quoteExpiresAt;
}
