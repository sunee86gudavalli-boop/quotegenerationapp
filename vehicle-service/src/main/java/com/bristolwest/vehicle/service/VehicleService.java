package com.bristolwest.vehicle.service;

import com.bristolwest.vehicle.dto.VehicleRequestDTO;
import com.bristolwest.vehicle.dto.VehicleResponseDTO;

import java.util.List;

public interface VehicleService {
    VehicleResponseDTO createVehicle(VehicleRequestDTO request);
    VehicleResponseDTO getVehicleById(Long id);
    List<VehicleResponseDTO> getVehiclesByCustomerId(Long customerId);
}
