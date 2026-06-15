package com.bristolwest.quote.service;

import com.bristolwest.quote.client.CustomerServiceClient;
import com.bristolwest.quote.client.DriverServiceClient;
import com.bristolwest.quote.client.RatingServiceClient;
import com.bristolwest.quote.client.VehicleServiceClient;
import com.bristolwest.quote.dto.QuoteRequestDTO;
import com.bristolwest.quote.dto.QuoteResponseDTO;
import com.bristolwest.quote.entity.Quote;
import com.bristolwest.quote.kafka.QuoteEventProducer;
import com.bristolwest.quote.kafka.QuoteGeneratedEvent;
import com.bristolwest.quote.repository.QuoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuoteServiceImpl implements QuoteService {

    private final QuoteRepository quoteRepository;
    private final CustomerServiceClient customerServiceClient;
    private final VehicleServiceClient vehicleServiceClient;
    private final DriverServiceClient driverServiceClient;
    private final RatingServiceClient ratingServiceClient;
    private final QuoteEventProducer quoteEventProducer;

    @Override
    @Transactional
    public QuoteResponseDTO generateQuote(QuoteRequestDTO request) {
        log.info("Generating quote for customer: {}", request.getEmail());

        // Step 1: Create customer
        CustomerServiceClient.CustomerRequest customerReq = new CustomerServiceClient.CustomerRequest();
        customerReq.setFirstName(request.getFirstName());
        customerReq.setLastName(request.getLastName());
        customerReq.setEmail(request.getEmail());
        customerReq.setPhone(request.getPhone());
        customerReq.setDateOfBirth(request.getDateOfBirth());
        customerReq.setAddress(request.getAddress());
        customerReq.setCity(request.getCity());
        customerReq.setState(request.getState());
        customerReq.setZipCode(request.getZipCode());
        CustomerServiceClient.CustomerResponse customer = customerServiceClient.createCustomer(customerReq);
        log.info("Customer created with id: {}", customer.getId());

        // Step 2: Create vehicle
        VehicleServiceClient.VehicleRequest vehicleReq = new VehicleServiceClient.VehicleRequest();
        vehicleReq.setCustomerId(customer.getId());
        vehicleReq.setVin(request.getVin());
        vehicleReq.setMake(request.getVehicleMake());
        vehicleReq.setModel(request.getVehicleModel());
        vehicleReq.setYear(request.getVehicleYear());
        vehicleReq.setUsageType(request.getUsageType() != null ? request.getUsageType() : "PERSONAL");
        vehicleReq.setAnnualMileage(request.getAnnualMileage());
        vehicleReq.setGaragingZip(request.getGaragingZip());
        VehicleServiceClient.VehicleResponse vehicle = vehicleServiceClient.createVehicle(vehicleReq);
        log.info("Vehicle created with id: {}", vehicle.getId());

        // Step 3: Create driver
        DriverServiceClient.DriverRequest driverReq = new DriverServiceClient.DriverRequest();
        driverReq.setCustomerId(customer.getId());
        driverReq.setFirstName(request.getFirstName());
        driverReq.setLastName(request.getLastName());
        driverReq.setDateOfBirth(request.getDriverDateOfBirth() != null
                ? request.getDriverDateOfBirth() : request.getDateOfBirth());
        driverReq.setLicenseNumber(request.getLicenseNumber());
        driverReq.setLicenseState(request.getLicenseState());
        driverReq.setYearsLicensed(request.getYearsLicensed());
        driverReq.setViolationsCount(request.getViolationsCount());
        driverReq.setAccidentsCount(request.getAccidentsCount());
        DriverServiceClient.DriverResponse driver = driverServiceClient.createDriver(driverReq);
        log.info("Driver created with id: {}", driver.getId());

        // Step 4: Calculate premium via rating service
        LocalDate dob = driver.getDateOfBirth() != null ? driver.getDateOfBirth() : request.getDateOfBirth();
        int driverAge = Period.between(dob, LocalDate.now()).getYears();

        RatingServiceClient.RatingRequest ratingReq = new RatingServiceClient.RatingRequest();
        ratingReq.setDriverAge(driverAge);
        ratingReq.setViolations(request.getViolationsCount());
        ratingReq.setAccidents(request.getAccidentsCount());
        ratingReq.setVehicleYear(request.getVehicleYear());
        ratingReq.setAnnualMileage(request.getAnnualMileage() != null ? request.getAnnualMileage() : 10000);
        ratingReq.setCoverageType(request.getCoverageType());
        ratingReq.setZipCode(request.getZipCode());
        RatingServiceClient.RatingResponse rating = ratingServiceClient.calculatePremium(ratingReq);
        log.info("Rating calculated. Total premium: {}", rating.getTotalPremium());

        // Step 5: Generate unique quote number
        String quoteNumber = generateQuoteNumber();

        // Step 6: Persist quote
        Quote quote = Quote.builder()
                .quoteNumber(quoteNumber)
                .customerId(customer.getId())
                .vehicleId(vehicle.getId())
                .driverId(driver.getId())
                .coverageType(Quote.CoverageType.valueOf(request.getCoverageType()))
                .basePremium(rating.getBasePremium())
                .liabilityPremium(rating.getLiabilityPremium())
                .collisionPremium(rating.getCollisionPremium())
                .comprehensivePremium(rating.getComprehensivePremium())
                .totalPremium(rating.getTotalPremium())
                .status(Quote.QuoteStatus.ACTIVE)
                .expiresAt(LocalDateTime.now().plusDays(30))
                .build();

        Quote saved = quoteRepository.save(quote);
        log.info("Quote saved with id: {} and number: {}", saved.getId(), saved.getQuoteNumber());

        // Step 7: Publish Kafka event
        QuoteGeneratedEvent event = QuoteGeneratedEvent.builder()
                .quoteId(saved.getId())
                .quoteNumber(saved.getQuoteNumber())
                .customerEmail(customer.getEmail())
                .customerName(customer.getFirstName() + " " + customer.getLastName())
                .totalPremium(saved.getTotalPremium())
                .coverageType(saved.getCoverageType().name())
                .vehicleMakeModel(vehicle.getYear() + " " + vehicle.getMake() + " " + vehicle.getModel())
                .quoteExpiresAt(saved.getExpiresAt())
                .build();
        quoteEventProducer.publishQuoteGeneratedEvent(event);

        return toResponse(saved, customer, vehicle);
    }

    @Override
    public QuoteResponseDTO getQuoteById(Long id) {
        Quote quote = quoteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quote not found with id: " + id));
        return toResponse(quote, null, null);
    }

    @Override
    public QuoteResponseDTO getQuoteByNumber(String quoteNumber) {
        Quote quote = quoteRepository.findByQuoteNumber(quoteNumber)
                .orElseThrow(() -> new RuntimeException("Quote not found with number: " + quoteNumber));
        return toResponse(quote, null, null);
    }

    @Override
    public List<QuoteResponseDTO> getQuotesByCustomerId(Long customerId) {
        return quoteRepository.findByCustomerId(customerId)
                .stream().map(q -> toResponse(q, null, null)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public QuoteResponseDTO updateQuoteStatus(Long id, String status) {
        Quote quote = quoteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quote not found with id: " + id));
        quote.setStatus(Quote.QuoteStatus.valueOf(status.toUpperCase()));
        return toResponse(quoteRepository.save(quote), null, null);
    }

    private String generateQuoteNumber() {
        String year = String.valueOf(LocalDate.now().getYear());
        String random = String.format("%06d", ThreadLocalRandom.current().nextInt(1, 999999));
        return "BW-" + year + "-" + random;
    }

    private QuoteResponseDTO toResponse(Quote q,
                                        CustomerServiceClient.CustomerResponse customer,
                                        VehicleServiceClient.VehicleResponse vehicle) {
        return QuoteResponseDTO.builder()
                .id(q.getId())
                .quoteNumber(q.getQuoteNumber())
                .customerId(q.getCustomerId())
                .vehicleId(q.getVehicleId())
                .driverId(q.getDriverId())
                .coverageType(q.getCoverageType().name())
                .basePremium(q.getBasePremium())
                .liabilityPremium(q.getLiabilityPremium())
                .collisionPremium(q.getCollisionPremium())
                .comprehensivePremium(q.getComprehensivePremium())
                .totalPremium(q.getTotalPremium())
                .status(q.getStatus().name())
                .createdAt(q.getCreatedAt())
                .expiresAt(q.getExpiresAt())
                .customerName(customer != null ? customer.getFirstName() + " " + customer.getLastName() : null)
                .customerEmail(customer != null ? customer.getEmail() : null)
                .vehicleMakeModel(vehicle != null ? vehicle.getMake() + " " + vehicle.getModel() : null)
                .vehicleYear(vehicle != null ? vehicle.getYear() : null)
                .build();
    }
}
