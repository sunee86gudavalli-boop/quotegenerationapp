package com.bristolwest.quote;

import com.bristolwest.quote.client.CustomerServiceClient;
import com.bristolwest.quote.client.DriverServiceClient;
import com.bristolwest.quote.client.RatingServiceClient;
import com.bristolwest.quote.client.VehicleServiceClient;
import com.bristolwest.quote.dto.QuoteRequestDTO;
import com.bristolwest.quote.dto.QuoteResponseDTO;
import com.bristolwest.quote.entity.Quote;
import com.bristolwest.quote.kafka.QuoteEventProducer;
import com.bristolwest.quote.repository.QuoteRepository;
import com.bristolwest.quote.service.QuoteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuoteServiceTest {

    @Mock private QuoteRepository quoteRepository;
    @Mock private CustomerServiceClient customerServiceClient;
    @Mock private VehicleServiceClient vehicleServiceClient;
    @Mock private DriverServiceClient driverServiceClient;
    @Mock private RatingServiceClient ratingServiceClient;
    @Mock private QuoteEventProducer quoteEventProducer;

    @InjectMocks
    private QuoteServiceImpl quoteService;

    private QuoteRequestDTO request;

    @BeforeEach
    void setUp() {
        request = new QuoteRequestDTO();
        request.setFirstName("Jane");
        request.setLastName("Smith");
        request.setEmail("jane.smith@example.com");
        request.setPhone("555-9876");
        request.setDateOfBirth(LocalDate.of(1990, 3, 20));
        request.setAddress("456 Oak Ave");
        request.setCity("Orlando");
        request.setState("FL");
        request.setZipCode("32801");
        request.setVehicleMake("Toyota");
        request.setVehicleModel("Camry");
        request.setVehicleYear(2022);
        request.setUsageType("PERSONAL");
        request.setAnnualMileage(12000);
        request.setGaragingZip("32801");
        request.setLicenseNumber("FL123456");
        request.setLicenseState("FL");
        request.setYearsLicensed(8);
        request.setViolationsCount(0);
        request.setAccidentsCount(0);
        request.setCoverageType("FULL");
    }

    @Test
    void generateQuote_success() {
        CustomerServiceClient.CustomerResponse customerResp = new CustomerServiceClient.CustomerResponse();
        customerResp.setId(1L);
        customerResp.setFirstName("Jane");
        customerResp.setLastName("Smith");
        customerResp.setEmail("jane.smith@example.com");

        VehicleServiceClient.VehicleResponse vehicleResp = new VehicleServiceClient.VehicleResponse();
        vehicleResp.setId(1L);
        vehicleResp.setMake("Toyota");
        vehicleResp.setModel("Camry");
        vehicleResp.setYear(2022);

        DriverServiceClient.DriverResponse driverResp = new DriverServiceClient.DriverResponse();
        driverResp.setId(1L);
        driverResp.setDateOfBirth(LocalDate.of(1990, 3, 20));

        RatingServiceClient.RatingResponse ratingResp = new RatingServiceClient.RatingResponse();
        ratingResp.setBasePremium(new BigDecimal("800.00"));
        ratingResp.setLiabilityPremium(new BigDecimal("320.00"));
        ratingResp.setCollisionPremium(new BigDecimal("280.00"));
        ratingResp.setComprehensivePremium(new BigDecimal("200.00"));
        ratingResp.setTotalPremium(new BigDecimal("800.00"));
        ratingResp.setCoverageType("FULL");

        Quote savedQuote = Quote.builder()
                .id(1L)
                .quoteNumber("BW-2026-000001")
                .customerId(1L)
                .vehicleId(1L)
                .driverId(1L)
                .coverageType(Quote.CoverageType.FULL)
                .basePremium(new BigDecimal("800.00"))
                .liabilityPremium(new BigDecimal("320.00"))
                .collisionPremium(new BigDecimal("280.00"))
                .comprehensivePremium(new BigDecimal("200.00"))
                .totalPremium(new BigDecimal("800.00"))
                .status(Quote.QuoteStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(30))
                .build();

        when(customerServiceClient.createCustomer(any())).thenReturn(customerResp);
        when(vehicleServiceClient.createVehicle(any())).thenReturn(vehicleResp);
        when(driverServiceClient.createDriver(any())).thenReturn(driverResp);
        when(ratingServiceClient.calculatePremium(any())).thenReturn(ratingResp);
        when(quoteRepository.save(any(Quote.class))).thenReturn(savedQuote);
        doNothing().when(quoteEventProducer).publishQuoteGeneratedEvent(any());

        QuoteResponseDTO response = quoteService.generateQuote(request);

        assertThat(response).isNotNull();
        assertThat(response.getQuoteNumber()).isEqualTo("BW-2026-000001");
        assertThat(response.getTotalPremium()).isEqualByComparingTo(new BigDecimal("800.00"));
        verify(quoteEventProducer, times(1)).publishQuoteGeneratedEvent(any());
    }

    @Test
    void getQuoteById_found() {
        Quote quote = Quote.builder()
                .id(1L)
                .quoteNumber("BW-2026-000001")
                .customerId(1L)
                .vehicleId(1L)
                .driverId(1L)
                .coverageType(Quote.CoverageType.FULL)
                .totalPremium(new BigDecimal("800.00"))
                .status(Quote.QuoteStatus.ACTIVE)
                .build();

        when(quoteRepository.findById(1L)).thenReturn(Optional.of(quote));

        QuoteResponseDTO response = quoteService.getQuoteById(1L);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getQuoteNumber()).isEqualTo("BW-2026-000001");
    }
}
