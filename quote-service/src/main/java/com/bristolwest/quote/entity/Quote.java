package com.bristolwest.quote.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "quotes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Quote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quote_number", nullable = false, unique = true, length = 20)
    private String quoteNumber;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "vehicle_id", nullable = false)
    private Long vehicleId;

    @Column(name = "driver_id", nullable = false)
    private Long driverId;

    @Enumerated(EnumType.STRING)
    @Column(name = "coverage_type", nullable = false)
    private CoverageType coverageType;

    @Column(name = "base_premium", precision = 10, scale = 2)
    private BigDecimal basePremium;

    @Column(name = "liability_premium", precision = 10, scale = 2)
    private BigDecimal liabilityPremium;

    @Column(name = "collision_premium", precision = 10, scale = 2)
    private BigDecimal collisionPremium;

    @Column(name = "comprehensive_premium", precision = 10, scale = 2)
    private BigDecimal comprehensivePremium;

    @Column(name = "total_premium", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPremium;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private QuoteStatus status = QuoteStatus.ACTIVE;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    public enum CoverageType {
        LIABILITY, STANDARD, FULL
    }

    public enum QuoteStatus {
        ACTIVE, EXPIRED, CONVERTED
    }
}
