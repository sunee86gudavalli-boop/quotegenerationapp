package com.bristolwest.quote.service;

import com.bristolwest.quote.dto.QuoteRequestDTO;
import com.bristolwest.quote.dto.QuoteResponseDTO;

import java.util.List;

public interface QuoteService {
    QuoteResponseDTO generateQuote(QuoteRequestDTO request);
    QuoteResponseDTO getQuoteById(Long id);
    QuoteResponseDTO getQuoteByNumber(String quoteNumber);
    List<QuoteResponseDTO> getQuotesByCustomerId(Long customerId);
    QuoteResponseDTO updateQuoteStatus(Long id, String status);
}
