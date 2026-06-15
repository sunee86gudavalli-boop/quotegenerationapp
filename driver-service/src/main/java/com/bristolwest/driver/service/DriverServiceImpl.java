package com.bristolwest.driver.service;

import com.bristolwest.driver.dto.DriverRequestDTO;
import com.bristolwest.driver.dto.DriverResponseDTO;
import com.bristolwest.driver.entity.Driver;
import com.bristolwest.driver.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;

    @Override
    public DriverResponseDTO createDriver(DriverRequestDTO request) {
        Driver driver = Driver.builder()
                .customerId(request.getCustomerId())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dateOfBirth(request.getDateOfBirth())
                .licenseNumber(request.getLicenseNumber())
                .licenseState(request.getLicenseState())
                .yearsLicensed(request.getYearsLicensed())
                .violationsCount(request.getViolationsCount())
                .accidentsCount(request.getAccidentsCount())
                .build();

        return toResponse(driverRepository.save(driver));
    }

    @Override
    public DriverResponseDTO getDriverById(Long id) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Driver not found with id: " + id));
        return toResponse(driver);
    }

    @Override
    public List<DriverResponseDTO> getDriversByCustomerId(Long customerId) {
        return driverRepository.findByCustomerId(customerId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    private DriverResponseDTO toResponse(Driver d) {
        return DriverResponseDTO.builder()
                .id(d.getId())
                .customerId(d.getCustomerId())
                .firstName(d.getFirstName())
                .lastName(d.getLastName())
                .dateOfBirth(d.getDateOfBirth())
                .licenseNumber(d.getLicenseNumber())
                .licenseState(d.getLicenseState())
                .yearsLicensed(d.getYearsLicensed())
                .violationsCount(d.getViolationsCount())
                .accidentsCount(d.getAccidentsCount())
                .createdAt(d.getCreatedAt())
                .build();
    }
}
