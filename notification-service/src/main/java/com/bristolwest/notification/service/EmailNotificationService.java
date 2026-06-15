package com.bristolwest.notification.service;

import com.bristolwest.notification.dto.QuoteGeneratedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationService {

    private final JavaMailSender mailSender;

    public void sendQuoteEmail(QuoteGeneratedEvent event) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(event.getCustomerEmail());
            message.setFrom("noreply@bristolwest.com");
            message.setSubject("Your Bristol West Auto Insurance Quote - " + event.getQuoteNumber());
            message.setText(buildEmailBody(event));
            mailSender.send(message);
            log.info("Quote email sent to {} for quote {}", event.getCustomerEmail(), event.getQuoteNumber());
        } catch (Exception e) {
            log.error("Failed to send email for quote {}: {}", event.getQuoteNumber(), e.getMessage());
        }
    }

    private String buildEmailBody(QuoteGeneratedEvent event) {
        return String.format("""
                Dear %s,

                Thank you for requesting an auto insurance quote from Bristol West (Farmers Insurance).

                Quote Details:
                ─────────────────────────────────────
                Quote Number  : %s
                Vehicle       : %s
                Coverage Type : %s
                Total Premium : $%.2f / year
                Valid Until   : %s
                ─────────────────────────────────────

                To convert this quote to a policy or for any questions,
                please contact us at 1-800-BRISTOL or visit bristolwest.com.

                Best regards,
                Bristol West Insurance Group
                A Farmers Insurance Company
                """,
                event.getCustomerName(),
                event.getQuoteNumber(),
                event.getVehicleMakeModel(),
                event.getCoverageType(),
                event.getTotalPremium(),
                event.getQuoteExpiresAt() != null ? event.getQuoteExpiresAt().toLocalDate() : "30 days"
        );
    }
}
