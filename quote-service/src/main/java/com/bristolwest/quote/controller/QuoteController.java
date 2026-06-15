package com.bristolwest.quote.controller;

import com.bristolwest.quote.dto.QuoteRequestDTO;
import com.bristolwest.quote.dto.QuoteResponseDTO;
import com.bristolwest.quote.service.QuoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quotes")
@RequiredArgsConstructor
public class QuoteController {

    private final QuoteService quoteService;

    @PostMapping
    public ResponseEntity<QuoteResponseDTO> generateQuote(@Valid @RequestBody QuoteRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(quoteService.generateQuote(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuoteResponseDTO> getQuoteById(@PathVariable Long id) {
        return ResponseEntity.ok(quoteService.getQuoteById(id));
    }

    @GetMapping("/number/{quoteNumber}")
    public ResponseEntity<QuoteResponseDTO> getQuoteByNumber(@PathVariable String quoteNumber) {
        return ResponseEntity.ok(quoteService.getQuoteByNumber(quoteNumber));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<QuoteResponseDTO>> getQuotesByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(quoteService.getQuotesByCustomerId(customerId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<QuoteResponseDTO> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(quoteService.updateQuoteStatus(id, body.get("status")));
    }
}
