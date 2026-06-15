package com.bristolwest.quote.repository;

import com.bristolwest.quote.entity.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, Long> {
    Optional<Quote> findByQuoteNumber(String quoteNumber);
    List<Quote> findByCustomerId(Long customerId);
}
