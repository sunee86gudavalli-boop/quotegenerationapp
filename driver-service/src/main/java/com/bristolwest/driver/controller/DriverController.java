package com.bristolwest.driver.controller;

import com.bristolwest.driver.dto.DriverRequestDTO;
import com.bristolwest.driver.dto.DriverResponseDTO;
import com.bristolwest.driver.service.DriverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    @PostMapping
    public ResponseEntity<DriverResponseDTO> createDriver(@Valid @RequestBody DriverRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(driverService.createDriver(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DriverResponseDTO> getDriver(@PathVariable Long id) {
        return ResponseEntity.ok(driverService.getDriverById(id));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<DriverResponseDTO>> getDriversByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(driverService.getDriversByCustomerId(customerId));
    }
}
