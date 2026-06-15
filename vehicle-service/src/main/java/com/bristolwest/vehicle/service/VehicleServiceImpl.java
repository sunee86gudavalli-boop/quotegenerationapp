package com.bristolwest.vehicle.service;

import com.bristolwest.vehicle.dto.VehicleRequestDTO;
import com.bristolwest.vehicle.dto.VehicleResponseDTO;
import com.bristolwest.vehicle.entity.Vehicle;
import com.bristolwest.vehicle.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;

    @Override
    public VehicleResponseDTO createVehicle(VehicleRequestDTO request) {
        Vehicle vehicle = Vehicle.builder()
                .customerId(request.getCustomerId())
                .vin(request.getVin())
                .make(request.getMake())
                .model(request.getModel())
                .year(request.getYear())
                .usageType(request.getUsageType())
                .annualMileage(request.getAnnualMileage())
                .garagingZip(request.getGaragingZip())
                .build();

        return toResponse(vehicleRepository.save(vehicle));
    }

    @Override
    public VehicleResponseDTO getVehicleById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + id));
        return toResponse(vehicle);
    }

    @Override
    public List<VehicleResponseDTO> getVehiclesByCustomerId(Long customerId) {
        return vehicleRepository.findByCustomerId(customerId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    private VehicleResponseDTO toResponse(Vehicle v) {
        return VehicleResponseDTO.builder()
                .id(v.getId())
                .customerId(v.getCustomerId())
                .vin(v.getVin())
                .make(v.getMake())
                .model(v.getModel())
                .year(v.getYear())
                .usageType(v.getUsageType())
                .annualMileage(v.getAnnualMileage())
                .garagingZip(v.getGaragingZip())
                .createdAt(v.getCreatedAt())
                .build();
    }
}
