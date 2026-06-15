package com.bristolwest.vehicle.controller;

import com.bristolwest.vehicle.dto.VehicleRequestDTO;
import com.bristolwest.vehicle.dto.VehicleResponseDTO;
import com.bristolwest.vehicle.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<VehicleResponseDTO> createVehicle(@Valid @RequestBody VehicleRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vehicleService.createVehicle(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleResponseDTO> getVehicle(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.getVehicleById(id));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<VehicleResponseDTO>> getVehiclesByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(vehicleService.getVehiclesByCustomerId(customerId));
    }
}
