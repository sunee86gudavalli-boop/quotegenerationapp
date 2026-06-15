package com.bristolwest.rating.service;

import com.bristolwest.rating.dto.RatingRequestDTO;
import com.bristolwest.rating.dto.RatingResponseDTO;

public interface RatingService {
    RatingResponseDTO calculatePremium(RatingRequestDTO request);
}
