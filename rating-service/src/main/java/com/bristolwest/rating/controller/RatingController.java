package com.bristolwest.rating.controller;

import com.bristolwest.rating.dto.RatingRequestDTO;
import com.bristolwest.rating.dto.RatingResponseDTO;
import com.bristolwest.rating.service.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rating")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @PostMapping("/calculate")
    public ResponseEntity<RatingResponseDTO> calculatePremium(@Valid @RequestBody RatingRequestDTO request) {
        return ResponseEntity.ok(ratingService.calculatePremium(request));
    }
}
