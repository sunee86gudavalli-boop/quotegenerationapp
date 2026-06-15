package com.bristolwest.notification.kafka;

import com.bristolwest.notification.dto.QuoteGeneratedEvent;
import com.bristolwest.notification.service.EmailNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class QuoteEventConsumer {

    private final EmailNotificationService emailNotificationService;

    @KafkaListener(
            topics = "quote-generated",
            groupId = "notification-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeQuoteGeneratedEvent(QuoteGeneratedEvent event) {
        log.info("Received quote-generated event for quote: {}", event.getQuoteNumber());
        emailNotificationService.sendQuoteEmail(event);
    }
}
