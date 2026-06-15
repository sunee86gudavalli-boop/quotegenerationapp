package com.bristolwest.rating.service;

import com.bristolwest.rating.dto.RatingRequestDTO;
import com.bristolwest.rating.dto.RatingResponseDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final EntityManager entityManager;

    @Override
    public RatingResponseDTO calculatePremium(RatingRequestDTO request) {
        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("calculate_premium");

        // Input params
        query.registerStoredProcedureParameter("p_driver_age",    Integer.class,    ParameterMode.IN);
        query.registerStoredProcedureParameter("p_violations",    Integer.class,    ParameterMode.IN);
        query.registerStoredProcedureParameter("p_accidents",     Integer.class,    ParameterMode.IN);
        query.registerStoredProcedureParameter("p_vehicle_year",  Integer.class,    ParameterMode.IN);
        query.registerStoredProcedureParameter("p_annual_mileage",Integer.class,    ParameterMode.IN);
        query.registerStoredProcedureParameter("p_coverage_type", String.class,     ParameterMode.IN);
        query.registerStoredProcedureParameter("p_zip_code",      String.class,     ParameterMode.IN);

        // Output params
        query.registerStoredProcedureParameter("p_base_premium",          BigDecimal.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("p_liability_premium",     BigDecimal.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("p_collision_premium",     BigDecimal.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("p_comprehensive_premium", BigDecimal.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("p_total_premium",         BigDecimal.class, ParameterMode.OUT);

        query.setParameter("p_driver_age",     request.getDriverAge());
        query.setParameter("p_violations",     request.getViolations());
        query.setParameter("p_accidents",      request.getAccidents());
        query.setParameter("p_vehicle_year",   request.getVehicleYear());
        query.setParameter("p_annual_mileage", request.getAnnualMileage());
        query.setParameter("p_coverage_type",  request.getCoverageType());
        query.setParameter("p_zip_code",       request.getZipCode() != null ? request.getZipCode() : "");

        query.execute();

        BigDecimal basePremium          = (BigDecimal) query.getOutputParameterValue("p_base_premium");
        BigDecimal liabilityPremium     = (BigDecimal) query.getOutputParameterValue("p_liability_premium");
        BigDecimal collisionPremium     = (BigDecimal) query.getOutputParameterValue("p_collision_premium");
        BigDecimal comprehensivePremium = (BigDecimal) query.getOutputParameterValue("p_comprehensive_premium");
        BigDecimal totalPremium         = (BigDecimal) query.getOutputParameterValue("p_total_premium");

        return RatingResponseDTO.builder()
                .basePremium(basePremium)
                .liabilityPremium(liabilityPremium)
                .collisionPremium(collisionPremium)
                .comprehensivePremium(comprehensivePremium)
                .totalPremium(totalPremium)
                .coverageType(request.getCoverageType())
                .build();
    }
}
