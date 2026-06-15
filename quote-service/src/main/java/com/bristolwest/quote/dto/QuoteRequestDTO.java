package com.bristolwest.quote.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;

@Data
public class QuoteRequestDTO {

    // Customer info
    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email
    private String email;

    private String phone;

    @NotNull(message = "Date of birth is required")
    @Past
    private LocalDate dateOfBirth;

    private String address;
    private String city;
    private String state;
    private String zipCode;

    // Vehicle info
    private String vin;

    @NotBlank(message = "Vehicle make is required")
    private String vehicleMake;

    @NotBlank(message = "Vehicle model is required")
    private String vehicleModel;

    @NotNull(message = "Vehicle year is required")
    @Min(value = 1900)
    private Integer vehicleYear;

    private String usageType;

    @Min(value = 0)
    private Integer annualMileage;

    private String garagingZip;

    // Driver info
    private String licenseNumber;
    private String licenseState;
    private LocalDate driverDateOfBirth;

    @Min(value = 0)
    private Integer yearsLicensed;

    @Min(value = 0)
    private Integer violationsCount = 0;

    @Min(value = 0)
    private Integer accidentsCount = 0;

    // Coverage
    @NotBlank(message = "Coverage type is required")
    private String coverageType;  // LIABILITY, STANDARD, FULL
}
