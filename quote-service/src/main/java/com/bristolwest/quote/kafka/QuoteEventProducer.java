package com.bristolwest.quote.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class QuoteEventProducer {

    private static final String TOPIC = "quote-generated";

    private final KafkaTemplate<String, QuoteGeneratedEvent> kafkaTemplate;

    public void publishQuoteGeneratedEvent(QuoteGeneratedEvent event) {
        log.info("Publishing quote event for quoteNumber: {}", event.getQuoteNumber());
        kafkaTemplate.send(TOPIC, event.getQuoteNumber(), event);
    }
}
