package com.bristolwest.driver.service;

import com.bristolwest.driver.dto.DriverRequestDTO;
import com.bristolwest.driver.dto.DriverResponseDTO;

import java.util.List;

public interface DriverService {
    DriverResponseDTO createDriver(DriverRequestDTO request);
    DriverResponseDTO getDriverById(Long id);
    List<DriverResponseDTO> getDriversByCustomerId(Long customerId);
}
