import axios from 'axios';

const API_BASE_URL = 'http://localhost:8088/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: { 'Content-Type': 'application/json' },
  timeout: 30000,
});

export const generateQuote = async (formData) => {
  const payload = {
    // Customer
    firstName: formData.firstName,
    lastName: formData.lastName,
    email: formData.email,
    phone: formData.phone,
    dateOfBirth: formData.dateOfBirth,
    address: formData.address,
    city: formData.city,
    state: formData.state,
    zipCode: formData.zipCode,
    // Vehicle
    vin: formData.vin,
    vehicleMake: formData.vehicleMake,
    vehicleModel: formData.vehicleModel,
    vehicleYear: parseInt(formData.vehicleYear),
    usageType: formData.usageType,
    annualMileage: parseInt(formData.annualMileage),
    garagingZip: formData.garagingZip,
    // Driver
    licenseNumber: formData.licenseNumber,
    licenseState: formData.licenseState,
    driverDateOfBirth: formData.driverDateOfBirth || formData.dateOfBirth,
    yearsLicensed: parseInt(formData.yearsLicensed),
    violationsCount: parseInt(formData.violationsCount) || 0,
    accidentsCount: parseInt(formData.accidentsCount) || 0,
    // Coverage
    coverageType: formData.coverageType,
  };

  const response = await api.post('/quotes', payload);
  return response.data;
};

export const getQuoteById = async (id) => {
  const response = await api.get(`/quotes/${id}`);
  return response.data;
};

export const getQuoteByNumber = async (quoteNumber) => {
  const response = await api.get(`/quotes/number/${quoteNumber}`);
  return response.data;
};
